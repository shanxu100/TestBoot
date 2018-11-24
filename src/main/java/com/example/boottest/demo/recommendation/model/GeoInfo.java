package com.example.boottest.demo.recommendation.model;

import java.util.List;

/**
 * @author Guan
 * @date Created on 2018/11/23
 */
public class GeoInfo extends BaseInfo {
    public String status;

    public String infocode;

    public Regeocode regeocode;


    public static class Regeocode {

        public String formatted_address;


        public List<Aoi> aois;

    }

    /**
     * AOI(Area of Interest),
     * 区别于POI(Point of Interest)
     */
    public static class Aoi {
        public String id;
        public String name;
        public String adcode;
        public String location;
        public String area;
        public String distance;
        /**
         * POI分类编码
         */
        public String type;

    }
}
