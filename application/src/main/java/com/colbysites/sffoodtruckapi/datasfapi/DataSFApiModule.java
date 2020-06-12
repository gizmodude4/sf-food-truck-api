package com.colbysites.sffoodtruckapi.datasfapi;

import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

@Module
public class DataSFApiModule {
  private String host;

  public DataSFApiModule(String host) {
    this.host = host;
  }

  @Provides
  @Singleton
  DataSFApiClient getDataSFApiClient(Retrofit retrofit) {
    return retrofit.create(DataSFApiClient.class);
  }

  @Provides
  @Singleton
  ScalarsConverterFactory getJacksonConverterFactory() {
    return ScalarsConverterFactory.create();
  }

  @Provides
  @Singleton
  CsvStringConverter getCsvStringConverter() {
    return new CsvStringConverter();
  }

  @Provides
  @Singleton
  Retrofit getRetrofit(ScalarsConverterFactory jacksonConverterFactory) {
    return new Retrofit.Builder()
        .baseUrl(host)
        .addConverterFactory(jacksonConverterFactory)
        .build();
  }
}
