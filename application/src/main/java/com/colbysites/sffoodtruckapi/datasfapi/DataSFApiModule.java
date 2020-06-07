package com.colbysites.sffoodtruckapi.datasfapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import javax.inject.Singleton;

@Module
public class DataSFApiModule {
    private String host;
    private ObjectMapper mapper;

    public DataSFApiModule(String host, ObjectMapper mapper) {
        this.host = host;
        this.mapper = mapper;
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
