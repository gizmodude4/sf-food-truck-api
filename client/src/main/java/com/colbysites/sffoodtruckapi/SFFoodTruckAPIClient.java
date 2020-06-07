package com.colbysites.sffoodtruckapi;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

import java.util.List;

public interface SFFoodTruckAPIClient {

    @GET("foodtruck")
    Call<List<FoodTruck>> getNearestFoodTrucks(@Query("lat") double lat,
                                               @Query("lon") double lon,
                                               @Query("type") List<TruckType> types,
                                               @Query("limit") long limit);
}