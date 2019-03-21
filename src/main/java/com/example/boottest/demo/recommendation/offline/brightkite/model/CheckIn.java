package com.example.boottest.demo.recommendation.offline.brightkite.model;

/**
 * @author Guan
 * @date Created on 2019/3/19
 */
public class CheckIn {

    protected String id;
    private String time;
    protected double longitude;
    protected double latitude;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public CheckIn() {
    }

    public CheckIn(String id, String time, double longitude, double latitude) {
        this.id = id;
        this.time = time;
        this.longitude = longitude;
        this.latitude = latitude;
    }
}