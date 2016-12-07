package com.example.yangseung.myapplication;

/**
 * Created by Sang-Won Yeo on 2016-11-07.
 */

public class MarkerItem {


    double lat;
    double lon;
    String positionName;


    public MarkerItem(double lat, double lon, String positionName) {
        this.lat = lat;
        this.lon = lon;
        this.positionName = positionName;

    }


    public double getLat() {
        return lat;
    }


    public void setLat(double lat) {
        this.lat = lat;
    }


    public double getLon() {
        return lon;
    }


    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }
}
