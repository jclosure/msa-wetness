package com.joelholder;

/**
 * Created by joel on 2/15/16.
 */
public class PrecipitationRecord {


    int wban;
    int precipitation;
    int hour;

    public PrecipitationRecord(int wban, int hour, int precipitation){
        this.wban = wban;
        this.precipitation = precipitation;
        this.hour = hour;
    }


    public int getWban() {
        return wban;
    }

    public void setWban(int wban) {
        this.wban = wban;
    }

    public long getPrecipitation() {
        return precipitation;
    }

    public void setPrecipitation(int precipitation) {
        this.precipitation = precipitation;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }


}
