package com.example.boottest.demo.recommendation;


/**
 * @author Guan
 * @date Created on 2018/11/21
 */
public class ContextInfo extends BaseInfo {

    public String deviceId;

    public String appId;
    public String time;
    public long timestamp;
    public String versionCode;
    public String versionName;



    public DeviceInfo deviceInfo;

    public LocationInfo locationInfo;




//==========================================


    public static class DeviceInfo {
        public String brand;
        public String display;
        public String model;
        public String versionRelease;
        /**
         * 制造商
         */
        public String manufacturer;
        /**
         * sim卡提供商
         */
        public String simProvider;
        public int SDKInt;


    }

    public static class LocationInfo {
        /**
         * 纬度
         */
        public double latitude;
        /**
         * 经度
         */
        public double longitude;


        /**
         * 结构化地址信息
         */
        public String formattedAddress;

        /**
         *
         */
        public GeoInfo.Aoi aoi;

    }

}
