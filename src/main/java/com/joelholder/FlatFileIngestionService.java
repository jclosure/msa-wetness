package com.joelholder;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Created by joel on 2/8/16.
 */
public class FlatFileIngestionService {


/*    public List<WbanLocation> getWbanLocations(String filePath) throws IOException {

        List<WbanLocation> recordList = new ArrayList<WbanLocation>();

        File csvFile = new File(filePath);

        CsvMapper mapper = new CsvMapper();
        CsvSchema schema = CsvSchema.emptySchema().withHeader().withColumnSeparator('|'); // use first row as header; otherwise defaults are fine
        MappingIterator<Map<String,String>> it = mapper.readerFor(Map.class)
                .with(schema)
                .readValues(csvFile);

        while (it.hasNext()) {
            Map<String,String> rowAsMap = it.next();
            WbanLocation record = buildWbanLocation(rowAsMap);
            recordList.add(record);
        }
        return recordList;

    }


    public List<PopulationRecord> loadMsaRecords(String filePath) throws IOException {

        List<PopulationRecord> recordList = new ArrayList<PopulationRecord>();

        File csvFile = new File(filePath);

        CsvMapper mapper = new CsvMapper();
        CsvSchema schema = CsvSchema.emptySchema().withHeader(); // use first row as header; otherwise defaults are fine
        MappingIterator<Map<String,String>> it = mapper.readerFor(Map.class)
                .with(schema)
                .readValues(csvFile);

        while (it.hasNext()) {
            Map<String,String> rowAsMap = it.next();
            PopulationRecord record = buildRecord(rowAsMap);
            recordList.add(record);
        }
        return recordList;
    }*/

    public <T> List<T> load(String filePath, char seperator, Function<Map<String, String>, T> fn) throws IOException {

        List<T> recordList = new ArrayList<T>();

        File csvFile = new File(filePath);

        CsvMapper mapper = new CsvMapper();
        CsvSchema schema = CsvSchema.emptySchema().withHeader().withColumnSeparator(seperator);
        MappingIterator<Map<String,String>> it = mapper.readerFor(Map.class)
                .with(schema)
                .readValues(csvFile);

        while (it.hasNext()) {
            Map<String,String> rowAsMap = it.next();
            T record = fn.apply(rowAsMap);
            recordList.add(record);
        }
        return recordList;
    }


    private PopulationRecord buildRecord(Map<String, String> rowAsMap) {
        PopulationRecord record = new PopulationRecord();
        record.setMsa(rowAsMap.get("MSA"));
        record.setPopulation(Long.parseLong(rowAsMap.get("Population")));
        return record;
    }

    private WbanLocation buildWbanLocation(Map<String, String> rowAsMap) {
        WbanLocation record = new WbanLocation();
        record.setId(rowAsMap.get("WBAN_ID"));
        String location = rowAsMap.get("LOCATION");

        double[] coords =  Arrays.asList(location.split(" "))
                .stream()
                .mapToDouble(dms -> new Coordinate(dms).getDecimal()).toArray();

        record.setLatitude(coords[0]);
        record.setLongitude(coords[1]);

        return record;
    }


}
