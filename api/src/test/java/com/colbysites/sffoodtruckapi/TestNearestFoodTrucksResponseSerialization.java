package com.colbysites.sffoodtruckapi;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.testing.FixtureHelpers;
import java.io.IOException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestNearestFoodTrucksResponseSerialization {
  private static final long NUM_RESULTS = 10;
  private static final long START_FROM = 0;
  private static final long LIMIT = 5;
  private static final double INPUT_LATITUDE = 0.123;
  private static final double INPUT_LONGITUDE = 0.456;
  private static final String TRUCK_NAME = "Pat's Panini Partywagon";
  private static final TruckType TRUCK_TYPE = TruckType.TRUCK;
  private static final String LOCATION_DESCRIPTION = "North of Seventh and South of Ninth";
  private static final String ADDRESS = "888 Eighth";
  private static final String FOOD_ITEMS = "Paninis pressed to perfection";
  private static final double DISTANCE_IN_MILES = 1.789;
  private static final ObjectMapper MAPPER = Jackson.newObjectMapper();

  @Test
  public void testDeserializationFromJSON() throws IOException {
    NearestFoodTrucksResponse response =
        MAPPER.readValue(FixtureHelpers.fixture("fixtures/nearestfoodtrucksresponse.json"),
        NearestFoodTrucksResponse.class);
    assertEquals(NUM_RESULTS, response.getNumResults());
    assertEquals(START_FROM, response.getStartFrom());
    assertEquals(LIMIT, response.getLimit());
    assertEquals(INPUT_LATITUDE, response.getInputLatitude(), 0);
    assertEquals(INPUT_LONGITUDE, response.getInputLongitude(), 0);
    assertEquals(1, response.getResults().size());
    FoodTruck truck = response.getResults().get(0);
    assertEquals(TRUCK_NAME, truck.getName());
    assertEquals(TRUCK_TYPE, truck.getType());
    assertEquals(LOCATION_DESCRIPTION, truck.getLocationDescription());
    assertEquals(ADDRESS, truck.getAddress());
    assertEquals(FOOD_ITEMS, truck.getFoodItems());
    assertEquals(DISTANCE_IN_MILES, truck.getDistanceInMiles(), 0);
  }

  @Test
  public void testSerializeToJSON() throws IOException {
    FoodTruck foodTruck = new FoodTruck(TRUCK_NAME, TRUCK_TYPE, LOCATION_DESCRIPTION,
        ADDRESS, FOOD_ITEMS, DISTANCE_IN_MILES);
    NearestFoodTrucksResponse response = new NearestFoodTrucksResponse(NUM_RESULTS, START_FROM, LIMIT,
        INPUT_LATITUDE, INPUT_LONGITUDE, Lists.newArrayList(foodTruck));
    String json = MAPPER.writeValueAsString(response);

    JsonNode expectedNode = MAPPER.readTree(FixtureHelpers.fixture("fixtures/nearestfoodtrucksresponse.json"));
    JsonNode actualNode = MAPPER.readTree(json);
    assertEquals(expectedNode, actualNode);
  }
}