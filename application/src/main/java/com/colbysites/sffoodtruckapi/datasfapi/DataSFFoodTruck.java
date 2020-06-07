package com.colbysites.sffoodtruckapi.datasfapi;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DataSFFoodTruck {
  private final String locationId;
  private final String name;
  private final String type;
  private final String locationDescription;
  private final String address;
  private final String status;
  private final String foodItems;
  private final String lat;
  private final String lon;
  private final String expiration;


  /**
   * A POJO representation of the relevant fields from the CSV returned by dataSF that represent a food truck.
   * @param locationId String ID of the location
   * @param name String name of the location
   * @param type String type of truck. Possible values include "Truck", "Push Cart", and empty string
   * @param locationDescription String description of the truck's location
   * @param address String address of the truck
   * @param status String status of the food truck's application. Possible values are "APPROVED" and "REQUESTED"
   * @param foodItems String a sampling of the items the truck offers.
   * @param lat String latitude of the truck
   * @param lon String longitude of the truck
   * @param expiration String expiration date of permit
   */
  @JsonCreator
  public DataSFFoodTruck(@JsonProperty("locationid") String locationId,
                         @JsonProperty("Applicant") String name,
                         @JsonProperty("FacilityType") String type,
                         @JsonProperty("LocationDescription") String locationDescription,
                         @JsonProperty("Address") String address,
                         @JsonProperty("Status") String status,
                         @JsonProperty("FoodItems") String foodItems,
                         @JsonProperty("Latitude") String lat,
                         @JsonProperty("Longitude") String lon,
                         @JsonProperty("ExpirationDate") String expiration) {
    this.locationId = locationId;
    this.name = name;
    this.type = type;
    this.locationDescription = locationDescription;
    this.address = address;
    this.status = status;
    this.foodItems = foodItems;
    this.lat = lat;
    this.lon = lon;
    this.expiration = expiration;
  }

  @JsonProperty("locationId")
  public String getLocationId() {
    return locationId;
  }

  @JsonProperty("name")
  public String getName() {
    return name;
  }

  @JsonProperty("type")
  public String getType() {
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

  @JsonProperty("status")
  public String getStatus() {
    return status;
  }

  @JsonProperty("foodItems")
  public String getFoodItems() {
    return foodItems;
  }

  @JsonProperty("lat")
  public String getLat() {
    return lat;
  }

  @JsonProperty("lon")
  public String getLon() {
    return lon;
  }

  @JsonProperty("expiration")
  public String getExpiration() {
    return expiration;
  }
}
