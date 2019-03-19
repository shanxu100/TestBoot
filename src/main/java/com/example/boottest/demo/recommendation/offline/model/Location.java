package com.example.boottest.demo.recommendation.offline.model;

/**
 * @author Guan
 * @date Created on 2019/3/14
 */
public class Location implements Comparable<Location> {

    private String id;
    private double longitude;
    private double latitude;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public Location(String id, double latitude, double longitude) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
    }


    @Override
    public String toString() {
        return "Location{" +
                "id='" + id + '\'' +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                '}';
    }

    @Override
    public int compareTo(Location o) {
        return Integer.parseInt(this.id) > Integer.parseInt(o.id) ? 1 : -1;
    }
}
