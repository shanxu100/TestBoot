package com.example.boottest.demo.recommendation.offline.brightkite.model;

/**
 * @author Guan
 * @date Created on 2019/3/20
 */
public class CheckInCount extends CheckIn {

    protected int count;


    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public CheckInCount(String id, double longitude, double latitude, int count) {
        this.id = id;
        this.longitude = longitude;
        this.latitude = latitude;
        this.count = count;

    }

    public void countAddAdd() {
        count++;
    }

    public void countAddAdd(int maxValue) {
        count = count < maxValue ? count + 1 : count;

    }
}
