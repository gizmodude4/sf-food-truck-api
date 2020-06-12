package com.colbysites.sffoodtruckapi.utils;

import com.colbysites.sffoodtruckapi.datasfapi.DataSFFoodTruck;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

public class DateUtils {
  private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a");

  /**
   * Returns true if a given {@link com.colbysites.sffoodtruckapi.FoodTruck}'s permit has not expired.
   * @param truck {@link com.colbysites.sffoodtruckapi.FoodTruck} input truck
   * @return boolean True if the given food truck's expiration has not passed. False otherwise.
   */
  public static boolean isNotExpired(DataSFFoodTruck truck) {
    // Since the data source doesn't have to worry about timezones, they don't include them,
    // so we'll add in the time zone here so this should work no matter where it runs.
    TemporalAccessor accessor = formatter.parse(truck.getExpiration());
    LocalDateTime local = LocalDateTime.from(accessor);
    ZonedDateTime zoned = ZonedDateTime.of(local, ZoneId.of("America/Los_Angeles"));
    return Instant.now().isBefore(Instant.from(zoned));
  }
}
