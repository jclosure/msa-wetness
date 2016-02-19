package com.joelholder;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Created by joel on 2/8/16.
 */
public class FlatFileIngestionService {



    public <T> List<T> load(String filePath, char seperator, Function<Map<String, String>, T> fn) throws IOException {

        List<T> recordList = new ArrayList<T>();

        File delimitedFile = new File(filePath);

        CsvMapper mapper = new CsvMapper();
        CsvSchema schema = CsvSchema.emptySchema().withHeader().withColumnSeparator(seperator);
        MappingIterator<Map<String,String>> it = mapper.readerFor(Map.class)
                .with(schema)
                .readValues(delimitedFile);

        while (it.hasNext()) {
            Map<String,String> rowAsMap = it.next();
            T record = fn.apply(rowAsMap);
            recordList.add(record);
        }
        return recordList;
    }

    public Stream<String> streamFileLines(String filePath) throws FileNotFoundException {

        InputStream is = new FileInputStream(new File(filePath));
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        return br.lines();
    }

}
