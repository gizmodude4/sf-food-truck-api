package com.colbysites.sffoodtruckapi.datasfapi;

import io.dropwizard.testing.FixtureHelpers;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CsvStringConverterTest {

  public CsvStringConverter unit;

  @Before
  public void init() {
    unit = new CsvStringConverter();
  }

  @Test
  public void testConversion() {
    String csvString = FixtureHelpers.fixture("fixtures/onefoodtruck.csv");
    List<DataSFFoodTruck> results = unit.convertCsvStringToFoodTrucks(csvString);
    assertEquals(1, results.size());
    assertEquals("1334734", results.get(0).getLocationId());
    assertEquals("Rita's Catering", results.get(0).getName());
    assertEquals("Truck", results.get(0).getType());
    assertEquals("1028 MISSION ST", results.get(0).getAddress());
    assertEquals("07/15/2020 12:00:00 AM", results.get(0).getExpiration());
    assertEquals("Filipino Food", results.get(0).getFoodItems());
    assertEquals("37.7806943774082", results.get(0).getLat());
    assertEquals("-122.409668813219", results.get(0).getLon());
    assertEquals("MISSION ST: 06TH ST to 07TH ST (1000 - 1099)", results.get(0).getLocationDescription());
    assertEquals("APPROVED", results.get(0).getStatus());
  }

  @Test
  public void testConversionInvalidCsv() {
    String csvString = FixtureHelpers.fixture("fixtures/invalidfoodtrucks.csv");
    List<DataSFFoodTruck> trucks = unit.convertCsvStringToFoodTrucks(csvString);
    assertEquals(0, trucks.size());
  }
}
