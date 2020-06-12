package com.colbysites.sffoodtruckapi.utils;

import com.colbysites.sffoodtruckapi.datasfapi.DataSFFoodTruck;

public class LatLonUtils {

  /**
   * Validate whether a given latitude and longitude are valid. Throws if either argument is not valid.
   * @param lat double Latitude
   * @param lon double Longitude.
   * @throws IllegalArgumentException if either argument are invalid.
   */
  public static void validateLatLon(double lat, double lon) {
    if (lat < -90 || lat > 90) {
      throw new IllegalArgumentException("Lat must be between -90 and 90.");
    }
    if (lon < -180 || lon > 180) {
      throw new IllegalArgumentException("Lon must be between -180 and 180.");
    }
  }

  /**
   * This function implements the Haversine formula to calculate the distance between two points on a sphere
   * given their latitudes and longitudes. Returns distance in miles as the crow flies.
   * Find out more about this neat algorithm here -- https://en.wikipedia.org/wiki/Haversine_formula
   * @param truck {@link com.colbysites.sffoodtruckapi.FoodTruck} truck to calculate distance for
   * @param inLat double Latitude to calculate distance from
   * @param inLon double Longitude to calculate distance from
   * @return double Distance in miles of input lat/lon to food truck.
   */
  public static double getDistance(DataSFFoodTruck truck, double inLat, double inLon) {
    final double earthRadiusInMiles = 3958.8;
    double truckLat = Double.valueOf(truck.getLat());
    double truckLon = Double.valueOf(truck.getLon());
    double latDifference = Math.toRadians(inLat - truckLat);
    double lonDistance = Math.toRadians(inLon - truckLon);
    double havLat = Math.pow(Math.sin(latDifference / 2), 2);
    double havLon = Math.pow(Math.sin(lonDistance / 2), 2);
    double a = havLat + Math.cos(Math.toRadians(truckLat)) * Math.cos(Math.toRadians(inLat)) * havLon;
    return 2 * earthRadiusInMiles * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
  }
}
