package com.colbysites.sffoodtruckapi;

import com.colbysites.sffoodtruckapi.datasfapi.DataSFApiModule;
import io.dropwizard.Application;
import io.dropwizard.configuration.ResourceConfigurationSourceProvider;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.federecio.dropwizard.swagger.SwaggerBundle;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;


public class FoodTruckApplication extends Application<FoodTruckConfiguration> {
  public static void main(String[] args) throws Exception {
    new FoodTruckApplication().run(args);
  }

  @Override
  public void initialize(Bootstrap<FoodTruckConfiguration> bootstrap) {
    initConfigResourceProvider(bootstrap);
    initSwagger(bootstrap);
  }

  @Override
  public void run(FoodTruckConfiguration configuration, Environment environment) throws Exception {
    FoodTruckComponent component = DaggerFoodTruckComponent.builder()
            .dataSFApiModule(new DataSFApiModule(configuration.getDataSFHost(), environment.getObjectMapper()))
            .build();

    environment.jersey().register(component.getFoodTruckResource());
  }

  private void initConfigResourceProvider(Bootstrap<FoodTruckConfiguration> bootstrap) {
    bootstrap.setConfigurationSourceProvider(new ResourceConfigurationSourceProvider());
  }

  private void initSwagger(Bootstrap<FoodTruckConfiguration> bootstrap){
    bootstrap.addBundle(new SwaggerBundle<FoodTruckConfiguration>() {
      @Override
      protected SwaggerBundleConfiguration getSwaggerBundleConfiguration(FoodTruckConfiguration configuration) {
        return configuration.swaggerBundleConfiguration;
      }
    });
  }
}
