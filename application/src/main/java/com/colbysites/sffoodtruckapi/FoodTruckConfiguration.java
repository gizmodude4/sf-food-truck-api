package com.colbysites.sffoodtruckapi;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;

public class FoodTruckConfiguration extends Configuration {
  @JsonProperty("dataSFHost")
  private String dataSFHost;

  @JsonProperty("swagger")
  public SwaggerBundleConfiguration swaggerBundleConfiguration;

  FoodTruckConfiguration(@JsonProperty("dataSFHost") String dataSFHost) {
    this.dataSFHost = dataSFHost;
  }

  public String getDataSFHost() {
    return this.dataSFHost;
  }
}
