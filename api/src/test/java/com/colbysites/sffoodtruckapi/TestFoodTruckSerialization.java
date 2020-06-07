package com.colbysites.sffoodtruckapi;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.testing.FixtureHelpers;
import java.io.IOException;
import org.junit.Test;

import static com.colbysites.sffoodtruckapi.TruckType.PUSH_CART;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestFoodTruckSerialization {
    private static final ObjectMapper MAPPER = Jackson.newObjectMapper();

    @Test
    public void testDeserializationFromJSON() throws IOException {
        FoodTruck truck = MAPPER.readValue(FixtureHelpers.fixture("fixtures/foodtruck.json"), FoodTruck.class);
        assertEquals("Via 313", truck.getName());
        assertEquals(PUSH_CART, truck.getType());
        assertEquals("Near the corner", truck.getLocationDescription());
        assertEquals("Detroit style pizza", truck.getFoodItems());
        assertEquals("123 Main St.", truck.getAddress());
        assertTrue(!truck.getDistanceInMiles().isNaN());
        assertTrue(truck.getDistanceInMiles().equals(1.1));
    }

    @Test
    public void testSerializeToJSON() throws IOException {
        FoodTruck user = new FoodTruck("Via 313", PUSH_CART, "Near the corner",
                "123 Main St.", "Detroit style pizza", 1.1);
        String json = MAPPER.writeValueAsString(user);

        JsonNode expectedNode = MAPPER.readTree(FixtureHelpers.fixture("fixtures/foodtruck.json"));
        JsonNode actualNode = MAPPER.readTree(json);
        assertEquals(expectedNode, actualNode);
    }
}