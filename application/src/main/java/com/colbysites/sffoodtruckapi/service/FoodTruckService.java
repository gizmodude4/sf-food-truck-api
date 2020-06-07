package com.colbysites.sffoodtruckapi.service;

import com.colbysites.sffoodtruckapi.FoodTruck;
import com.colbysites.sffoodtruckapi.TruckType;
import com.colbysites.sffoodtruckapi.datasfapi.CsvStringConverter;
import com.colbysites.sffoodtruckapi.datasfapi.DataSFApiClient;
import com.colbysites.sffoodtruckapi.datasfapi.DataSFFoodTruck;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import retrofit2.Call;
import retrofit2.Response;

import javax.inject.Inject;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


public class FoodTruckService {
    private static final String APPROVAL_STRING = "APPROVED";
    private final DataSFApiClient client;
    private final CsvStringConverter converter;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a");
    private final LoadingCache<String, List<DataSFFoodTruck>> foodTruckCache;

    @Inject
    public FoodTruckService(DataSFApiClient client, CsvStringConverter converter) {
        this.client = client;
        this.converter = converter;
        this.foodTruckCache = CacheBuilder.newBuilder()
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .maximumSize(1000)
                .build(
                        // This cache loader function doesn't actually use the key. I figure this gives
                        // us some room to grow in case we support other data APIs. If we deploy this with
                        // more than one instance, we should instead use a distributed cache system.
                        new CacheLoader<String, List<DataSFFoodTruck>>() {
                            @Override
                            public List<DataSFFoodTruck> load(String s) throws Exception {
                                return getFoodTrucksFromApi();
                            }
                        }
                );
    }

    public List<FoodTruck> getFoodTrucks(List<TruckType> types, double inLat, double inLon, long limit) throws IOException {
        if (limit < 1) {
            throw new IllegalArgumentException("Limit must be >= 1");
        }
        validateLatLon(inLat, inLon);
        try {
            return foodTruckCache.get("trucks").stream()
                    .filter(truck -> APPROVAL_STRING.equals(truck.getStatus()))          // Only return approved trucks
                    .filter(this::isNotExpired)                                          // Make sure application not expired
                    .map(truck -> toFoodTruck(truck, inLat, inLon))                      // Convert to API object
                    .filter(truck -> types.isEmpty() || types.contains(truck.getType())) // Filter to only requested types
                    .sorted(Comparator.comparing(FoodTruck::getDistanceInMiles))         // Sort by distance
                    .limit(limit)                                                        // limit
                    .collect(Collectors.toList());
        } catch (ExecutionException e) {
            // Most of the time, if this happens, it's because we couldn't contact the API. Throw an IOException.
            throw new IOException(e);
        }
    }

    private FoodTruck toFoodTruck(DataSFFoodTruck truck, double inLat, double inLon) {
        return new FoodTruck(truck.getName(), toTruckType(truck.getType()),
                truck.getLocationDescription(), truck.getAddress(),
                truck.getFoodItems(), getDistance(truck, inLat, inLon));
    }

    private TruckType toTruckType(String type) {
        switch(type) {
            case "Truck":
                return TruckType.TRUCK;
            case "Push Cart":
                return TruckType.PUSH_CART;
            default:
                return TruckType.UNKNOWN_TYPE;
        }
    }

    private List<DataSFFoodTruck> getFoodTrucksFromApi() throws IOException{
        Call<String> retrofitCall = client.getFoodTrucks();
        Response<String> response = retrofitCall.execute();
        if (!response.isSuccessful()) {
            throw new IOException("Call to " + retrofitCall.request().url() + "failed with code " +
                    response.code() + ". Message: " + response.message());
        }
        return converter.convertCsvStringToFoodTrucks(response.body());
    }

    private void validateLatLon(double lat, double lon) {
        if (lat < -90 || lat > 90) {
            throw new IllegalArgumentException("Lat must be between -90 and 90.");
        }
        if (lon < -180 || lon > 180) {
            throw new IllegalArgumentException("Lon must be between -180 and 180.");
        }
    }

    private boolean isNotExpired(DataSFFoodTruck truck) {
        // Since the data source doesn't have to worry about timezones, they don't include them,
        // so we'll add in the time zone here so this should work no matter where it runs.
        TemporalAccessor accessor = formatter.parse(truck.getExpiration());
        LocalDateTime local = LocalDateTime.from(accessor);
        ZonedDateTime zoned = ZonedDateTime.of(local, ZoneId.of("America/Los_Angeles"));
        return Instant.now().isBefore(Instant.from(zoned));
    }

    // This function implements the Haversine formula to calculate the distance between two points on a sphere
    // given their lats and lons. Returns distance in miles as the crow flies.
    // Find out more about this neat algo here -- https://en.wikipedia.org/wiki/Haversine_formula
    private Double getDistance(DataSFFoodTruck truck, double inLat, double inLon) {
        final double earthRadiusInMiles = 3958.8;
        double truckLat = Double.valueOf(truck.getLat());
        double truckLon = Double.valueOf(truck.getLon());
        double latDifference = Math.toRadians(inLat - truckLat);
        double lonDistance = Math.toRadians(inLon - truckLon);
        double havLat = Math.pow(Math.sin(latDifference/2), 2);
        double havLon = Math.pow(Math.sin(lonDistance/2), 2);
        double a = havLat + Math.cos(Math.toRadians(truckLat))*Math.cos(Math.toRadians(inLat))*havLon;
        return 2 * earthRadiusInMiles * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    }
}
