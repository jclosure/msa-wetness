package com.joelholder;

/**
 * Created by joelholder on 2/19/16.
 */
public class WetnessIndicator {
    PopulationRecord populationRecord;
    PrecipitationRecord precipitationRecord;
    WbanLocation wbanLocation;



    public WetnessIndicator(WbanLocation wbanLocation, PopulationRecord populationRecord, PrecipitationRecord precipitationRecord) {
        this.wbanLocation = wbanLocation;
        this.populationRecord = populationRecord;
        this.precipitationRecord = precipitationRecord;
    }

    public int computeWetness() {
        // todo: ...
        return 0;
    }
}
