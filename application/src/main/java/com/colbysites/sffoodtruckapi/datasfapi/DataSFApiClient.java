package com.colbysites.sffoodtruckapi.datasfapi;

import retrofit2.Call;
import retrofit2.http.GET;

public interface DataSFApiClient {
    @GET("api/views/rqzj-sfat/rows.csv")
    Call<String> getFoodTrucks();
}
