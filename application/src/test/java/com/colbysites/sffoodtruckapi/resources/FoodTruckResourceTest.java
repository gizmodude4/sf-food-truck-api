package com.colbysites.sffoodtruckapi.resources;

import com.colbysites.sffoodtruckapi.FoodTruck;
import com.colbysites.sffoodtruckapi.NearestFoodTrucksResponse;
import com.colbysites.sffoodtruckapi.TruckType;
import com.colbysites.sffoodtruckapi.service.FoodTruckService;
import com.google.common.collect.Lists;
import java.io.IOException;
import javax.ws.rs.BadRequestException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class FoodTruckResourceTest {
  private static final long DEFAULT_LIMIT = 5L;
  private static final long DEFAULT_START = 0L;
  private static final String TRUCK_NAME = "Jonald Hamboni's Calzoney Zambonie";
  private static final TruckType TRUCK_TYPE = TruckType.TRUCK;
  private static final String LOCATION_DESCRIPTION = "Near the good mall";
  private static final String ADDRESS = "456 Mall Dr.";
  private static final String FOOD_ITEMS = "Calzones and calzone adjacent fare like empanadas";
  private static final Double DISTANCE = 0.0123;

  private static final double LAT = 0.0;
  private static final double LON = 100.0;

  private final FoodTruckService foodTruckService = mock(FoodTruckService.class);

  private FoodTruckResource unit;

  @Before
  public void init() {
    initMocks(this);
    unit = new FoodTruckResource(foodTruckService);
  }

  @Test
  public void testGet() throws IOException {
    FoodTruck truck = buildMockFoodTruck();
    when(foodTruckService.getFoodTrucks(anyList(), anyDouble(), anyDouble())).thenReturn(Lists.newArrayList(truck));
    NearestFoodTrucksResponse actual = unit.getFoodTruckIds(LAT, LON, Lists.newArrayList(TRUCK_TYPE), 0L, 5L);
    assertEquals(1, actual.getNumResults());
    assertTruckEquals(truck, actual.getResults().get(0));
  }

  @Test
  public void testGetNoLimitDefaults() throws IOException {
    FoodTruck truck = buildMockFoodTruck();
    when(foodTruckService.getFoodTrucks(anyList(), anyDouble(), anyDouble()))
        .thenReturn(Lists.newArrayList(truck, truck, truck, truck, truck, truck, truck));
    NearestFoodTrucksResponse actual = unit.getFoodTruckIds(LAT, LON, Lists.newArrayList(TRUCK_TYPE), null, null);
    verify(foodTruckService).getFoodTrucks(Lists.newArrayList(TRUCK_TYPE), LAT, LON);
    assertEquals(DEFAULT_LIMIT, actual.getLimit());
    assertEquals(DEFAULT_START, actual.getStartFrom());
  }

  @Test(expected = BadRequestException.class)
  public void testGetThrowsOnIllegalArgument() throws IOException {
    when(foodTruckService.getFoodTrucks(anyList(), anyDouble(), anyDouble()))
        .thenThrow(new IllegalArgumentException("You did a bad thing!"));
    unit.getFoodTruckIds(LAT, LON, Lists.newArrayList(TRUCK_TYPE), null, null);
  }

  @Test(expected = BadRequestException.class)
  public void testGetThrowsOnUnspecifiedLat() {
    unit.getFoodTruckIds(null, LON, Lists.newArrayList(TRUCK_TYPE), null, null);
  }

  @Test(expected = BadRequestException.class)
  public void testGetThrowsOnUnspecifiedLon() {
    unit.getFoodTruckIds(LAT, null, Lists.newArrayList(TRUCK_TYPE), null, null);
  }

  @Test(expected = BadRequestException.class)
  public void testGetThrowsOnTooFewResults() throws IOException {
    FoodTruck truck = buildMockFoodTruck();
    when(foodTruckService.getFoodTrucks(anyList(), anyDouble(), anyDouble())).thenReturn(Lists.newArrayList(truck));
    NearestFoodTrucksResponse actual = unit.getFoodTruckIds(LAT, LON, Lists.newArrayList(TRUCK_TYPE), 1L, 5L);
    assertEquals(1, actual.getNumResults());
    assertTruckEquals(truck, actual.getResults().get(0));
  }

  private FoodTruck buildMockFoodTruck() {
    return new FoodTruck(TRUCK_NAME, TRUCK_TYPE, LOCATION_DESCRIPTION, ADDRESS, FOOD_ITEMS, DISTANCE);
  }

  private void assertTruckEquals(FoodTruck expected, FoodTruck actual) {
    assertEquals(expected.getDistanceInMiles(), actual.getDistanceInMiles());
    assertEquals(expected.getAddress(), actual.getAddress());
    assertEquals(expected.getFoodItems(), actual.getFoodItems());
    assertEquals(expected.getLocationDescription(), actual.getLocationDescription());
    assertEquals(expected.getName(), actual.getName());
    assertEquals(expected.getType(), actual.getType());
  }
}
