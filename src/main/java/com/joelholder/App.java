package com.joelholder;

import utils.WorksheetReader;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by joel on 2/15/16.
 */
public class App {

    FlatFileIngestionService flatFileIngestionService = new FlatFileIngestionService();

    public static void main(String[] args) {
        try {
            new App().start();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }


    @SuppressWarnings("Duplicates")
    public void start() throws IOException {

        try {

            List<PopulationRecord> populationRecords =  this.flatFileIngestionService.load( "./datasets/msa_populations.csv", ',', rowAsMap -> {
                PopulationRecord record = new PopulationRecord();
                record.setMsa(rowAsMap.get("MSA"));
                record.setPopulation(Long.parseLong(rowAsMap.get("Population")));
                return record;
            });

            List<WbanLocation> locations =  this.flatFileIngestionService.load("./datasets/wbanmasterlist.psv", '|', rowAsMap -> {
                WbanLocation record = new WbanLocation();
                record.setId(rowAsMap.get("WBAN_ID"));
                String location = rowAsMap.get("LOCATION");

                double[] coords;
                if (location.matches(Coordinate.dmsPattern1.toString())) {
                    coords = Arrays.asList(location.split(" "))
                            .stream()
                            .mapToDouble(dms -> new Coordinate(dms).getDecimal()).toArray();

                    record.setLatitude(coords[0]);
                    record.setLongitude(coords[1]);
                }
                else if (location.matches(Coordinate.dmsPattern2.toString())){
                    coords = null;
                }

                return record;
            });





            // rainfall here.
            String precipFilePath = "./datasets/2015precip.csv";


            String workbookFilePath = "datasets/List2.xls";
            String worksheetName = "List 2";

            Map<String, String> data =  WorksheetReader.read(workbookFilePath, worksheetName);

            String asdf = "";

           /* List<PopulationRecord> records = streamFile(precipFilePath)
                    .substream(1)
                    .map(App::msaRowMapper)
                    .filter(person -> person.getAge() > 17)
                    .limit(50)
                    .collect(toList());

            records.stream().map(record -> {

                record.getPrecipitation();
            });
            */

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

   /* private Stream<String> streamFile(String filePath) throws FileNotFoundException {

        InputStream is = new FileInputStream(new File(filePath));
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        return br.lines();
    }

    private PopulationRecord populationRecordMapper(String row) {
        PopulationRecord record = new PopulationRecord();

        record.setMsa(rowAsMap.get("MSA"));
        record.setPopulation(Long.parseLong(rowAsMap.get("Population")));
        return record;
    }

    private PopulationRecord populationRecordMapper(String row) {
        PopulationRecord record = new PopulationRecord();

        record.setMsa(rowAsMap.get("MSA"));
        record.setPopulation(Long.parseLong(rowAsMap.get("Population")));
        return record;
    }*/

}
