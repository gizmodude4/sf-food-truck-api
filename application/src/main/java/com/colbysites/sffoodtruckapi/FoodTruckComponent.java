package com.colbysites.sffoodtruckapi;

import com.colbysites.sffoodtruckapi.datasfapi.DataSFApiModule;
import com.colbysites.sffoodtruckapi.resources.FoodTruckResource;
import com.colbysites.sffoodtruckapi.resources.PingResource;
import dagger.Component;
import javax.inject.Singleton;

@Singleton
@Component(modules = {DataSFApiModule.class})
public interface FoodTruckComponent {
  FoodTruckResource getFoodTruckResource();

  PingResource getPingResource();
}
