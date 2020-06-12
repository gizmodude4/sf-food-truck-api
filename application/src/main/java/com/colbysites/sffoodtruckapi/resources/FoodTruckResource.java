package com.colbysites.sffoodtruckapi.resources;

import com.codahale.metrics.annotation.ResponseMetered;
import com.codahale.metrics.annotation.Timed;
import com.colbysites.sffoodtruckapi.FoodTruck;
import com.colbysites.sffoodtruckapi.NearestFoodTrucksResponse;
import com.colbysites.sffoodtruckapi.TruckType;
import com.colbysites.sffoodtruckapi.service.FoodTruckService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.ServiceUnavailableException;
import javax.ws.rs.core.MediaType;

import static java.util.Objects.isNull;

@Api(value = "foodtrucks",
        description = "API to get San Francisco food trucks closest to your location")
@Path("/foodtrucks")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class FoodTruckResource {
  private static final long DEFAULT_LIMIT = 5;
  private static final long DEFAULT_START_FROM = 0;
  private final FoodTruckService foodTruckService;

  @Inject
  FoodTruckResource(FoodTruckService foodTruckService) {
    this.foodTruckService = foodTruckService;
  }

  /**
   * Given a latitude, longitude, optional limit, and optional filter, return a list of the
   * closest San Francisco food trucks to the input coordinate.
   * @param lat Double latitude
   * @param lon Double longitude
   * @param types List<{@link TruckType}> list of types to include. Empty list applies no filter.
   * @param limit Long how many trucks to return. Default is 5
   * @return {@link NearestFoodTrucksResponse} response containing list of nearest food trucks and metadata
   */
  @ApiOperation(value = "Get list of closest SF food trucks given lat and lon",
          response = FoodTruck.class, responseContainer = "List")
  @ApiResponses(value = {
          @ApiResponse(code = 200,
                  message = "Successfully got an ordered list of closest food trucks to supplied lat and lon"),
          @ApiResponse(code = 400,
                  message = "Either lat, lon, or limit are invalid"),
          @ApiResponse(code = 503, message = "Cannot contact dataSF api")
  })
  @Timed
  @ResponseMetered
  @GET
  public NearestFoodTrucksResponse getFoodTruckIds(@ApiParam(name = "lat", value = "Your longitude", type = "double")
                                           @QueryParam("lat") Double lat,
                                         @ApiParam(name = "lon", value = "Your longitude", type = "double")
                                           @QueryParam("lon") Double lon,
                                         @ApiParam(name = "types", value = "Type of food truck to filter to",
                                                 type = "string", allowMultiple = true)
                                           @QueryParam("type") List<TruckType> types,
                                         @ApiParam(name = "startFrom", value = "Start position in results to return",
                                             defaultValue = "0", type = "long")
                                           @QueryParam("startFrom") Long startFrom,
                                         @ApiParam(name = "limit", value = "How many to return",
                                                 defaultValue = "5", type = "long")
                                           @QueryParam("limit") Long limit) {
    if (limit == null) {
      limit = DEFAULT_LIMIT;
    }

    if (isNull(startFrom)) {
      startFrom = DEFAULT_START_FROM;
    }

    if (isNull(lat) || isNull(lon)) {
      throw new BadRequestException("Must specify lat and lon");
    }

    if (limit < 1) {
      throw new BadRequestException("Limit must be > 0");
    }

    try {
      List<FoodTruck> results = foodTruckService.getFoodTrucks(types, lat, lon);
      if (results.size() <= startFrom) {
        throw new BadRequestException("Number of results is less than requested start position");
      }

      return new NearestFoodTrucksResponse(results.size(), startFrom, limit, lat, lon,
          results.stream().skip(startFrom).limit(limit).collect(Collectors.toList()));
    } catch (IOException e) {
      throw new ServiceUnavailableException("Can not contact dataSF api");
    } catch (IllegalArgumentException e) {
      throw new BadRequestException("Invalid latitude or longitude found", e);
    }
  }
}
