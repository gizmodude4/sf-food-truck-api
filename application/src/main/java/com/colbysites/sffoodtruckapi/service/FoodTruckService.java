package com.colbysites.sffoodtruckapi.service;

import com.colbysites.sffoodtruckapi.FoodTruck;
import com.colbysites.sffoodtruckapi.TruckType;
import com.colbysites.sffoodtruckapi.datasfapi.CsvStringConverter;
import com.colbysites.sffoodtruckapi.datasfapi.DataSFApiClient;
import com.colbysites.sffoodtruckapi.datasfapi.DataSFFoodTruck;
import com.colbysites.sffoodtruckapi.utils.DateUtils;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Call;
import retrofit2.Response;

import static com.colbysites.sffoodtruckapi.utils.LatLonUtils.getDistance;
import static com.colbysites.sffoodtruckapi.utils.LatLonUtils.validateLatLon;

public class FoodTruckService {
  private static final Logger LOGGER = LoggerFactory.getLogger(FoodTruckService.class);
  private static final String APPROVAL_STRING = "APPROVED";
  private final DataSFApiClient client;
  private final CsvStringConverter converter;

  private final LoadingCache<String, List<DataSFFoodTruck>> foodTruckCache;

  /**
   * FoodTruckService is the service layer that abstracts away and caches food truck data from the dataSF API
   * as well as provides a method to get nearest food trucks from that API.
   * @param client {@link DataSFApiClient} Retrofit client to talk with the dataSF API
   * @param converter {@link CsvStringConverter} Converter for CSV string to POJO
   */
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
              /**
               * Method to call when cache misses.
               * @param s String currently unused but can specify what API to talk to
               * @return List<{@link DataSFFoodTruck}> list of food trucks returned from method.
               * @throws ExecutionException If unable to talk with dataSF API or something else goes wrong.
               */
              @Override
              public List<DataSFFoodTruck> load(String s) throws Exception {
                return getFoodTrucksFromApi();
              }
            }
        );
  }

  /**
   * Method to return list of closest food trucks to a given lat and long with a filter and limit applied. Only
   * trucks that have an approved permit (status == "APPROVED"), expiration date is in the future, and optionally
   * of the specified type.
   * @param types List<{@link TruckType}> list of types to return. Empty list means no filter applied
   * @param inLat double latitude to center search on
   * @param inLon double longitude to center search on
   * @return List<{@link FoodTruck}> List of the closest trucks to supplied lat/lon in ascending order of distance
   * @throws IOException when something goes wrong either talking to the dataSF API or caching the results.
   */
  public List<FoodTruck> getFoodTrucks(List<TruckType> types, double inLat, double inLon) throws IOException {
    validateLatLon(inLat, inLon);
    try {
      return foodTruckCache.get("trucks").stream()
          .filter(truck -> APPROVAL_STRING.equals(truck.getStatus()))          // Only return approved trucks
          .filter(DateUtils::isNotExpired)                                     // Make sure application not expired
          .map(truck -> toFoodTruck(truck, inLat, inLon))                      // Convert to API object
          .filter(truck -> types.isEmpty() || types.contains(truck.getType())) // Filter to only requested types
          .sorted(Comparator.comparing(FoodTruck::getDistanceInMiles))         // Sort by distance
          .collect(Collectors.toList());
    } catch (ExecutionException e) {
      LOGGER.error("Could not get food trucks to cache", e);
      // Most of the time, if this happens, it's because we couldn't contact the API. Throw an IOException.
      throw new IOException(e);
    }
  }

  private List<DataSFFoodTruck> getFoodTrucksFromApi() throws IOException {
    Call<String> retrofitCall = client.getFoodTrucks();
    Response<String> response = retrofitCall.execute();
    if (!response.isSuccessful()) {
      throw new IOException("Call to " + retrofitCall.request().url() + "failed with code "
          + response.code() + ". Message: " + response.message());
    }
    return converter.convertCsvStringToFoodTrucks(response.body());
  }

  private FoodTruck toFoodTruck(DataSFFoodTruck truck, double inLat, double inLon) {
    return new FoodTruck(truck.getName(), toTruckType(truck.getType()),
        truck.getLocationDescription(), truck.getAddress(),
        truck.getFoodItems(), getDistance(truck, inLat, inLon));
  }

  private TruckType toTruckType(String type) {
    switch (type) {
      case "Truck":
        return TruckType.TRUCK;
      case "Push Cart":
        return TruckType.PUSH_CART;
      default:
        return TruckType.UNKNOWN_TYPE;
    }
  }
}
