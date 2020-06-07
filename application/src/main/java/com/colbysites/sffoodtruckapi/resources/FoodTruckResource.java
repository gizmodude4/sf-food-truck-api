package com.colbysites.sffoodtruckapi.resources;

import com.colbysites.sffoodtruckapi.FoodTruck;
import com.colbysites.sffoodtruckapi.TruckType;
import com.colbysites.sffoodtruckapi.service.FoodTruckService;
import io.swagger.annotations.*;

import java.io.IOException;
import java.util.List;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import static java.util.Objects.isNull;

/*
TODO:
- add linting
- update README
- test out Docker build/run
- add more Swagger annotations
- test test test
 */

@Api(value = "foodtrucks", description = "API to get San Francisco food trucks closest to your location")
@Path("/foodtrucks")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class FoodTruckResource {
  private static final long DEFAULT_LIMIT = 5;
  private final FoodTruckService foodTruckService;

  @Inject
  FoodTruckResource(FoodTruckService foodTruckService) {
    this.foodTruckService = foodTruckService;
  }


  @ApiOperation(value = "Get list of closest SF food trucks given lat and lon", response = FoodTruck.class, responseContainer = "List")
  @ApiResponses( value = {
          @ApiResponse(code = 200, message = "Successfully got an ordered list of closest food trucks to supplied lat and lon"),
          @ApiResponse(code = 400, message = "Either lat, lon, or limit are invalid"),
          @ApiResponse(code = 503, message = "Cannot contact dataSF api")
  })
  @GET
  public List<FoodTruck> getFoodTruckIds(@ApiParam(name="lat", value = "Your longitude", type= "double")
                                           @QueryParam("lat") Double lat,
                                         @ApiParam(name="lon", value = "Your longitude", type= "double")
                                           @QueryParam("lon") Double lon,
                                         @ApiParam(name="types", value = "Type of food truck to filter to", type= "string", allowMultiple = true)
                                           @QueryParam("type") List<TruckType> types,
                                         @ApiParam(name="limit", value = "How many to return", defaultValue = "5", type= "long")
                                           @QueryParam("limit") Long limit) {
    if (limit == null) {
      limit = DEFAULT_LIMIT;
    }

    if (isNull(lat) || isNull(lon)) {
      throw new BadRequestException("Must specify lat and lon");
    }

    try {
      return foodTruckService.getFoodTrucks(types, lat, lon, limit);
    } catch (IOException e) {
      throw new ServiceUnavailableException("Can not contact dataSF api");
    } catch (IllegalArgumentException e) {
      throw new BadRequestException("Invalid latitude, longitude, or limit found", e);
    }
  }
}
