package com.colbysites.sffoodtruckapi.utils;

import com.colbysites.sffoodtruckapi.datasfapi.DataSFFoodTruck;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LatLonUtilsTest {
  private static final String TRUCK_ID = "1234";
  private static final String TRUCK_NAME = "Jonald Hamboni's Calzoney Zambonie";
  private static final String TRUCK_TYPE = "Truck";
  private static final String LOCATION_DESCRIPTION = "Near the good mall";
  private static final String ADDRESS = "456 Mall Dr.";
  private static final String TRUCK_STATUS = "APPROVED";
  private static final String FOOD_ITEMS = "Calzones and calzone adjacent fare like empanadas";
  private static final double LAT = 37.7806943774082;
  private static final double LON = -122.409668813219;
  private static final String EXPIRATION_TIME = "2019-07-03,0,07/15/2020 12:00:00 AM";

  @Test
  public void verifyZeroDistance() {
    DataSFFoodTruck testTruck = new DataSFFoodTruck(TRUCK_ID, TRUCK_NAME, TRUCK_TYPE, LOCATION_DESCRIPTION, ADDRESS,
        TRUCK_STATUS, FOOD_ITEMS, String.valueOf(LAT), String.valueOf(LON), EXPIRATION_TIME);
    double distance = LatLonUtils.getDistance(testTruck, LAT, LON);
    assertEquals(0.0, distance, 0);
  }

  @Test
  public void verifyDistanceFromZeroZero() {
    // Calculated this using https://www.nhc.noaa.gov/gccalc.shtml
    // It's a little different from what we'd expect because the earth isn't a sphere and Haversine assumes a sphere.
    // We'll make sure it's within an error margin of 0.01%.
    double expectedDistance = 7944.8521;
    DataSFFoodTruck testTruck = new DataSFFoodTruck(TRUCK_ID, TRUCK_NAME, TRUCK_TYPE, LOCATION_DESCRIPTION, ADDRESS,
        TRUCK_STATUS, FOOD_ITEMS, String.valueOf(LAT), String.valueOf(LON), EXPIRATION_TIME);
    double distance = LatLonUtils.getDistance(testTruck, 0.0, 0.0);
    assertEquals(expectedDistance, distance, expectedDistance * 0.001);
  }
}
