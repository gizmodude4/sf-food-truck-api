package com.colbysites.sffoodtruckapi.resources;

import com.colbysites.sffoodtruckapi.FoodTruck;
import com.colbysites.sffoodtruckapi.TruckType;
import com.colbysites.sffoodtruckapi.service.FoodTruckService;
import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.BadRequestException;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class FoodTruckResourceTest {
    private static final long DEFAULT_LIMIT = 5L;
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
        when(foodTruckService.getFoodTrucks(anyList(), anyDouble(), anyDouble(), anyLong())).thenReturn(Lists.newArrayList(truck));
        List<FoodTruck> actual = unit.getFoodTruckIds(LAT, LON, Lists.newArrayList(TRUCK_TYPE), 1L);
        assertEquals(1, actual.size());
        assertTruckEquals(truck, actual.get(0));
    }

    @Test
    public void testGetNoLimitDefaults() throws IOException {
        FoodTruck truck = buildMockFoodTruck();
        when(foodTruckService.getFoodTrucks(anyList(), anyDouble(), anyDouble(), anyLong())).thenReturn(Lists.newArrayList(truck, truck, truck, truck, truck, truck, truck));
        unit.getFoodTruckIds(LAT, LON, Lists.newArrayList(TRUCK_TYPE), null);
        verify(foodTruckService).getFoodTrucks(Lists.newArrayList(TRUCK_TYPE), LAT, LON, DEFAULT_LIMIT);
    }

    @Test(expected = BadRequestException.class)
    public void testGetThrowsOnIllegalArgument() throws IOException {
        when(foodTruckService.getFoodTrucks(anyList(), anyDouble(), anyDouble(), anyLong())).thenThrow(new IllegalArgumentException("You did a bad thing!"));
        unit.getFoodTruckIds(LAT, LON, Lists.newArrayList(TRUCK_TYPE), null);
    }

    @Test(expected = BadRequestException.class)
    public void testGetThrowsOnUnspecifiedLat() {
        unit.getFoodTruckIds(null, LON, Lists.newArrayList(TRUCK_TYPE), null);
    }

    @Test(expected = BadRequestException.class)
    public void testGetThrowsOnUnspecifiedLon() {
        unit.getFoodTruckIds(LAT, null, Lists.newArrayList(TRUCK_TYPE), null);
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
