package com.colbysites.sffoodtruckapi.service;

import com.colbysites.sffoodtruckapi.FoodTruck;
import com.colbysites.sffoodtruckapi.TruckType;
import com.colbysites.sffoodtruckapi.datasfapi.CsvStringConverter;
import com.colbysites.sffoodtruckapi.datasfapi.DataSFApiClient;
import com.colbysites.sffoodtruckapi.datasfapi.DataSFFoodTruck;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvParser;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.google.common.collect.Lists;
import io.dropwizard.testing.FixtureHelpers;
import java.io.IOException;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import retrofit2.Call;
import retrofit2.Response;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class FoodTruckServiceTest {
  private static final CsvMapper CSV_MAPPER = new CsvMapper().enable(CsvParser.Feature.IGNORE_TRAILING_UNMAPPABLE);
  private static final ObjectReader OBJECT_READER = CSV_MAPPER.readerFor(DataSFFoodTruck.class)
      .with(CsvSchema.emptySchema().withHeader());
  private static final double LAT = 37.7806943774082;
  private static final double LON = -122.409668813219;
  private static final double INVALID_LAT = 100.0;
  private static final double INVALID_LON = 200.0;

  private final DataSFApiClient dataClient = mock(DataSFApiClient.class);
  private final Call<String> dataClientCall = mock(Call.class);
  private final CsvStringConverter converter = mock(CsvStringConverter.class);

  private FoodTruckService unit;

  /**
   * Init method called before tests.
   */
  @Before
  public void init() {
    initMocks(this);
    when(dataClient.getFoodTrucks()).thenReturn(dataClientCall);
    unit = new FoodTruckService(dataClient, converter);
  }

  // The test data in foodtrucks.csv provides data for the following cases:
  // 3 - APPROVED, not expired, Truck type
  // 1 - APPROVED, not expired, Push Cart type
  // 1 - APPROVED, not expired, no type
  // 1 - APPROVED, expired, Truck type
  // 1 - REQUESTED, not expired, no type
  // 1 - REQUESTED, not expired, Truck type
  // 1 - REQUESTED, not expired, Push Cart type
  @Test
  public void getFoodTrucksNoFilters() throws IOException {
    String csvString = FixtureHelpers.fixture("fixtures/foodtrucks.csv");
    when(dataClientCall.execute()).thenReturn(Response.success(csvString));
    when(converter.convertCsvStringToFoodTrucks(csvString)).thenReturn(convertCsvStringToFoodTrucks(csvString));
    List<FoodTruck> trucks = unit.getFoodTrucks(Lists.newArrayList(), LAT, LON);
    verify(dataClient).getFoodTrucks();
    verify(converter).convertCsvStringToFoodTrucks(csvString);
    assertEquals(5, trucks.size());
  }

  @Test
  public void getFoodTrucksJustTrucks() throws IOException {
    String csvString = FixtureHelpers.fixture("fixtures/foodtrucks.csv");
    when(dataClientCall.execute()).thenReturn(Response.success(csvString));
    when(converter.convertCsvStringToFoodTrucks(csvString)).thenReturn(convertCsvStringToFoodTrucks(csvString));
    List<FoodTruck> trucks = unit.getFoodTrucks(Lists.newArrayList(TruckType.TRUCK), LAT, LON);
    verify(dataClient).getFoodTrucks();
    verify(converter).convertCsvStringToFoodTrucks(csvString);
    assertEquals(3, trucks.size());
  }

  @Test
  public void getFoodTrucksJustCarts() throws IOException {
    String csvString = FixtureHelpers.fixture("fixtures/foodtrucks.csv");
    when(dataClientCall.execute()).thenReturn(Response.success(csvString));
    when(converter.convertCsvStringToFoodTrucks(csvString)).thenReturn(convertCsvStringToFoodTrucks(csvString));
    List<FoodTruck> trucks = unit.getFoodTrucks(Lists.newArrayList(TruckType.PUSH_CART), LAT, LON);
    verify(dataClient).getFoodTrucks();
    verify(converter).convertCsvStringToFoodTrucks(csvString);
    assertEquals(1, trucks.size());
  }

  @Test
  public void getFoodTrucksJustUnknowns() throws IOException {
    String csvString = FixtureHelpers.fixture("fixtures/foodtrucks.csv");
    when(dataClientCall.execute()).thenReturn(Response.success(csvString));
    when(converter.convertCsvStringToFoodTrucks(csvString)).thenReturn(convertCsvStringToFoodTrucks(csvString));
    List<FoodTruck> trucks = unit.getFoodTrucks(Lists.newArrayList(TruckType.UNKNOWN_TYPE), LAT, LON);
    verify(dataClient).getFoodTrucks();
    verify(converter).convertCsvStringToFoodTrucks(csvString);
    assertEquals(1, trucks.size());
  }

  @Test(expected = IOException.class)
  public void getFoodTrucksThrowsIfAPIDown() throws IOException {
    when(dataClientCall.execute()).thenThrow(new IOException("Oh man, that API is goooone"));
    unit.getFoodTrucks(Lists.newArrayList(), LAT, LON);
  }

  @Test(expected = IllegalArgumentException.class)
  public void getFoodTrucksThrowsForInvalidLat() throws IOException {
    unit.getFoodTrucks(Lists.newArrayList(), INVALID_LAT, LON);
  }

  @Test(expected = IllegalArgumentException.class)
  public void getFoodTrucksThrowsForInvalidLon() throws IOException {
    unit.getFoodTrucks(Lists.newArrayList(), LAT, INVALID_LON);
  }

  private List<DataSFFoodTruck> convertCsvStringToFoodTrucks(String csv) {
    try {
      MappingIterator<DataSFFoodTruck> it = OBJECT_READER.readValues(csv);
      return it.readAll();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
