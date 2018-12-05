package com.example.boottest.demo.recommendation.model;


import com.example.boottest.demo.recommendation.ctx.ContextConstant;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 记录最基本的情景信息
 *
 * @author Guan
 * @date Created on 2018/11/21
 */
public class ContextInfo extends BaseInfo {

    private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    public String deviceId;
    public String appId;

    public String itemId;
    public int rating;
    public long duration;
    public int wordCount;

    public String time;
    public String timeSegment;
    public long timestamp;

    public String versionCode;
    public String versionName;


    public DeviceInfo deviceInfo;

    public LocationInfo locationInfo;


    /**
     * 经过 地理逆编码 后，补充地点信息
     *
     * @param formattedAddress
     * @param poiType
     * @param poiTypeName
     */
    public void addLocationExtentedInfo(String formattedAddress, String poiType, String poiTypeName) {
        locationInfo.formattedAddress = formattedAddress;
        locationInfo.poiType = poiType;
        locationInfo.poiTypeName = poiTypeName;
        locationInfo.placeType = ContextConstant.getPlaceType(poiType);
        locationInfo.placeTypeName = ContextConstant.getPlaceTypeName(poiType);
    }

    /**
     * 获取情景Id
     *
     * @return
     */
    public String getContextId() {
        return ContextConstant.getContextId(this);
    }

    /**
     * 格式化时间信息
     */
    public void formatTime() {
        timestamp = timestamp <= 0 ? System.currentTimeMillis() : timestamp;
        time = format.format(new Date(timestamp));
        int hour = Integer.parseInt(time.substring(11, 13));
        if (hour >= 6 && hour < 12) {
            timeSegment = "上午";
        } else if (hour >= 12 && hour < 18) {
            timeSegment = "下午";
        } else {
            timeSegment = "晚上";
        }
    }


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


        //====================================
        /**
         * 结构化地址信息
         */
        public String formattedAddress;

        /**
         * 从服务器获取的AOI的信息
         */
        public String poiType;
        public String poiTypeName;

        /**
         * 将AOI信息简化为本系统使用的场所信息
         */
        public String placeType;
        public String placeTypeName;


    }

}
