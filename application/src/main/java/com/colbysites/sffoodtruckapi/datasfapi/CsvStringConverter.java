package com.colbysites.sffoodtruckapi.datasfapi;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvParser;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import java.io.IOException;
import java.util.List;

public class CsvStringConverter {
    private final CsvMapper mapper;
    private final ObjectReader reader;

    public CsvStringConverter(){
        this.mapper = new CsvMapper().enable(CsvParser.Feature.IGNORE_TRAILING_UNMAPPABLE);
        this.reader = mapper.readerFor(DataSFFoodTruck.class).with(CsvSchema.emptySchema().withHeader());
    }

    public List<DataSFFoodTruck> convertCsvStringToFoodTrucks(String csv) {
        try {
            MappingIterator<DataSFFoodTruck> it = reader.readValues(csv);
            return it.readAll();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
