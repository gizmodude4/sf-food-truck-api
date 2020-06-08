package com.colbysites.sffoodtruckapi.healthchecks;

import com.codahale.metrics.health.HealthCheck;
import java.net.HttpURLConnection;
import java.net.URL;

public class DataSFHealthCheck extends HealthCheck {
  private static final String TRUCKS_API_ENDPOINT = "api/views/rqzj-sfat/rows.csv";
  private final String baseApiUrl;

  public DataSFHealthCheck(String baseApiUrl) {
    this.baseApiUrl = baseApiUrl;
  }

  @Override
  protected Result check() throws Exception {
    URL url = new URL(baseApiUrl + TRUCKS_API_ENDPOINT);
    HttpURLConnection huc = (HttpURLConnection) url.openConnection();
    huc.setRequestMethod("OPTIONS");

    int responseCode = huc.getResponseCode();
    if (HttpURLConnection.HTTP_OK == responseCode) {
      return Result.healthy("Access to dataSF API established");
    }
    return Result.unhealthy("Could not establish access to dataSF API");
  }
}
