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
