package com.joelholder;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
                record.setId(Integer.parseInt(rowAsMap.get("WBAN_ID")));
                String location = rowAsMap.get("LOCATION");

                // todo: work on better regexes
                try {
                    double[] coords;
                    if (location.matches(Coordinate.dmsPattern1.toString())) {
                        // handle coordinates in dms form
                        coords = Arrays.asList(location.split(" "))
                                .stream()
                                .mapToDouble(dms -> new Coordinate(dms, Coordinate.dmsPattern1).getDecimal()).toArray();
                    } else {
                        // handle coordinates already in decimal form
                        coords = Arrays.asList(location.split(" "))
                                .stream()
                                .mapToDouble(sCoord -> Double.parseDouble(sCoord)).toArray();
                    }

                    record.setLatitude(coords[0]);
                    record.setLongitude(coords[1]);
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                }

                return record;
            });


            // todo: next we will map MSA to FIPS via the coordinates


            // todo: then we will stream and filter the large rainfall dataset.
            String precipFilePath = "./datasets/201505precip.csv";


            // todo: here we will
            // todo: then we will compute rainfall relative to adjusted population for April + 1 month (May)


            // grouping and summing
            flatFileIngestionService.streamFileLines(precipFilePath)
                    .skip(1)
                    .map(line -> {
                        // todo: project out a PrecipitationRecord for each line
                        String[] values = line.split(",");
                        int wban = Integer.parseInt(values[0]);
                        int hour = Integer.parseInt(values[2]);
                        int precip = 0;
                        try {
                             precip = Integer.parseInt(values[3]);
                        }
                        catch (Exception ex) {} // else noop

                        return new PrecipitationRecord(wban, hour, precip);
                    })
                    // hours between 12am and 7am don't count
                    .filter(precip -> precip.getHour() > 7)
                    .map(precip -> {

                        WbanLocation loc = locations.stream()
                                .peek(loc1 -> System.out.println("will filter " + loc1.getId()))
                                .filter(loc2 -> loc2.getId() == precip.getWban())
                                .findFirst().get();

                        /*PopulationRecord a = populationRecords.stream()
                                .peek(pop -> System.out.println("will filter " + pop.getMsa()))
                                .filter(pop -> pop.getMsa() == precip.getWban())
                                .findFirst()*/

                        // todo: combine WbanLocation with PopulationRecord and project out a WetnessIndicator

                        return precip;

                    })
                    // group by msa and get the average rainfall of each MSA
                    .collect(Collectors.groupingBy(PrecipitationRecord::getWban,
                            Collectors.averagingLong(PrecipitationRecord::getPrecipitation)))
                    .entrySet().stream()
                    // sort from wettest to dryest
                    .sorted(Map.Entry.comparingByValue())
                    .forEach(System.out::println);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }




}
