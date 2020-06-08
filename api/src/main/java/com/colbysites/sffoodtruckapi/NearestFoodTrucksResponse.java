package com.colbysites.sffoodtruckapi;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;

@ApiModel(description = "Return object of the nearest food trucks and metadata about the response")
public class NearestFoodTrucksResponse {
  @ApiModelProperty(value = "Number of results")
  private final long numResults;
  @ApiModelProperty(value = "What position the request started from")
  private final long startFrom;
  @ApiModelProperty(value = "Requested limit")
  private final long limit;
  @ApiModelProperty(value = "Requested latitude to center search on")
  private final double inputLatitude;
  @ApiModelProperty(value = "Requested longitude to center search on")
  private final double inputLongitude;
  @ApiModelProperty(value = "Resulting nearest food trucks")
  private final List<FoodTruck> results;

  /**
   * Return object of the nearest food trucks and metadata about the response.
   * @param numResults long Number of results
   * @param startFrom long Number of entries to skip before returning
   * @param limit long Number of entries to return
   * @param inputLatitude double Input latitude of request
   * @param inputLongitude double Input longitude of request
   * @param results List<@link FoodTruck> List of food trucks
   */
  @JsonCreator
  public NearestFoodTrucksResponse(@JsonProperty("numResults") long numResults,
                            @JsonProperty("startFrom") long startFrom,
                            @JsonProperty("limit") long limit,
                            @JsonProperty("inputLatitude") double inputLatitude,
                            @JsonProperty("inputLongitude") double inputLongitude,
                            @JsonProperty("results") List<FoodTruck> results) {
    this.numResults = numResults;
    this.startFrom = startFrom;
    this.limit = limit;
    this.inputLatitude = inputLatitude;
    this.inputLongitude = inputLongitude;
    this.results = results;
  }

  @JsonProperty("numResults")
  public long getNumResults() {
    return numResults;
  }

  @JsonProperty("startFrom")
  public long getStartFrom() {
    return startFrom;
  }

  @JsonProperty("limit")
  public long getLimit() {
    return limit;
  }

  @JsonProperty("inputLatitude")
  public double getInputLatitude() {
    return inputLatitude;
  }

  @JsonProperty("inputLongitude")
  public double getInputLongitude() {
    return inputLongitude;
  }

  @JsonProperty("results")
  public List<FoodTruck> getResults() {
    return results;
  }
}
