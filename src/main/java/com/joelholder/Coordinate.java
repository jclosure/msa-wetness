package com.joelholder;


import java.util.regex.Pattern;

public class Coordinate {

    public static final Pattern dmsPattern1 = Pattern.compile("[-0-9]+\\*[-0-9]+'[-0-9]+\"[NSEW]\\s[-0-9]+\\*[-0-9]+'[-0-9]+\"[NSEW]");

    public static final Pattern dmsPattern2 = Pattern.compile("[-0-9]+\\s[-0-9]+\\s[-0-9]+\\s[NSEW]/\\s[-0-9]+\\s[-0-9]+\\s[-0-9]+\\s[NSEW]");

    private int degrees;
    private int minutes;
    private int seconds;
    private double decimal;

    public Coordinate(String coordString, Pattern pattern) {

        coordString = coordString.replaceAll("\"", "");

        String degreesString = "";
        String minutesString = "";
        String secondsString = "";
        String directionString = "";

        // cut up the dmsString into parts
        if (pattern == dmsPattern1) {
            degreesString = coordString.replaceAll("(^.*)(\\*.*)", "$1");
            minutesString = coordString.replaceAll("([0-9]+\\*)([0-9]+)('.*)", "$2");
            secondsString = coordString.replaceAll("([0-9*]+)'([0-9]+)([NSEW])", "$2");
            directionString = coordString.replaceAll("([0-9*']+)([NSEW]$)", "$2");
        }
        else if (pattern == dmsPattern2) {
            degreesString = coordString.replaceAll("(^.*)(\\*.*)", "$1");
            minutesString = coordString.replaceAll("([0-9]+\\*)([0-9]+)('.*)", "$2");
            secondsString = coordString.replaceAll("([0-9*]+)'([0-9]+)([NSEW])", "$2");
            directionString = coordString.replaceAll("([0-9*']+)([NSEW]$)", "$2");
        }

        try {
            degrees = Integer.parseInt(degreesString);
            minutes = Integer.parseInt(minutesString);
            seconds = Integer.parseInt(secondsString);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Bad DMS input: " + coordString);
        }


        decimal = convertDmsToDecimal(degrees, minutes, seconds, directionString);
    }

    public double convertDmsToDecimal(int degrees, int minutes, int seconds, String direction) {

        // time scale settings
        int minutesToDegree = 60;
        int secondsToDegree = minutesToDegree * 60; // 3600

        // formula
        double totalSeconds = minutes * 60 + seconds;
        double totalSecondsContributedToDegrees = totalSeconds / secondsToDegree;
        double totalDegrees = degrees + totalSecondsContributedToDegrees;

        // if south or west degrees are negative
        totalDegrees = (direction.equals("S") || direction.equals("W")) ? totalDegrees * -1 : totalDegrees;

        return totalDegrees;
    }


    public int getDegrees() {
        return degrees;
    }

    public void setDegrees(int degrees) {
        this.degrees = degrees;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    public double getDecimal() {
        return decimal;
    }

    public void setDecimal(double decimal) {
        this.decimal = decimal;
    }
}