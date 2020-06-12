package com.colbysites.sffoodtruckapi.utils;

import com.colbysites.sffoodtruckapi.datasfapi.DataSFFoodTruck;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DateUtilsTest {
  private static final String TRUCK_ID = "1234";
  private static final String TRUCK_NAME = "Jonald Hamboni's Calzoney Zambonie";
  private static final String TRUCK_TYPE = "Truck";
  private static final String LOCATION_DESCRIPTION = "Near the good mall";
  private static final String ADDRESS = "456 Mall Dr.";
  private static final String TRUCK_STATUS = "APPROVED";
  private static final String FOOD_ITEMS = "Calzones and calzone adjacent fare like empanadas";
  private static final double LAT = 37.7806943774082;
  private static final double LON = -122.409668813219;
  private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a")
      .withZone(ZoneId.of("America/Los_Angeles"));

  @Test
  public void verifyBefore() {
    ZonedDateTime before = Instant.now().minusSeconds(10L).atZone(ZoneId.of("America/Los_Angeles"));
    DataSFFoodTruck testTruck = new DataSFFoodTruck(TRUCK_ID, TRUCK_NAME, TRUCK_TYPE, LOCATION_DESCRIPTION, ADDRESS,
        TRUCK_STATUS, FOOD_ITEMS, String.valueOf(LAT), String.valueOf(LON), formatter.format(before));
    assertFalse(DateUtils.isNotExpired(testTruck));
  }

  @Test
  public void verifyAfter() {
    ZonedDateTime after = Instant.now().plusSeconds(10L).atZone(ZoneId.of("America/Los_Angeles"));
    DataSFFoodTruck testTruck = new DataSFFoodTruck(TRUCK_ID, TRUCK_NAME, TRUCK_TYPE, LOCATION_DESCRIPTION, ADDRESS,
        TRUCK_STATUS, FOOD_ITEMS, String.valueOf(LAT), String.valueOf(LON), formatter.format(after));
    assertTrue(DateUtils.isNotExpired(testTruck));
  }
}
