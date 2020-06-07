package com.colbysites.sffoodtruckapi;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Return object representing a food truck")
public class FoodTruck {
  @ApiModelProperty(value = "Name of the food truck")
  private final String name;
  @ApiModelProperty(value = "Type of food truck")
  private final TruckType type;
  @ApiModelProperty(value = "Description of where the food truck is located")
  private final String locationDescription;
  @ApiModelProperty(value = "Food truck address")
  private final String address;
  @ApiModelProperty(value = "Brief description of what the food truck offers")
  private final String foodItems;
  @ApiModelProperty(value = "Distance away from supplied lat/lon")
  private final double distanceInMiles;

  /**
   * POJO representing a Food Truck.
   * @param name String name of the food truck
   * @param type {@link TruckType} type of the truck
   * @param locationDescription String description of the location of the truck
   * @param address String address of the truck
   * @param foodItems String sample of the things the truck offers
   * @param distanceInMiles Double distance away from input lat/lon in miles
   */
  @JsonCreator
  public FoodTruck(@JsonProperty("name") String name,
                   @JsonProperty("type") TruckType type,
                   @JsonProperty("locationDescription") String locationDescription,
                   @JsonProperty("address") String address,
                   @JsonProperty("foodItems") String foodItems,
                   @JsonProperty("distanceInMiles") double distanceInMiles) {
    this.name = name;
    this.type = type;
    this.locationDescription = locationDescription;
    this.address = address;
    this.foodItems = foodItems;
    this.distanceInMiles = distanceInMiles;
  }

  @JsonProperty("name")
  public String getName() {
    return name;
  }

  @JsonProperty("type")
  public TruckType getType() {
    return type;
  }

  @JsonProperty("locationDescription")
  public String getLocationDescription() {
    return locationDescription;
  }

  @JsonProperty("address")
  public String getAddress() {
    return address;
  }

  @JsonProperty("foodItems")
  public String getFoodItems() {
    return foodItems;
  }

  @JsonProperty("distanceInMiles")
  public Double getDistanceInMiles() {
    return distanceInMiles;
  }
}
