package com.example.boottest.demo.recommendation.geo;

import com.example.boottest.demo.recommendation.model.GeoInfo;
import com.example.boottest.demo.utils.GsonUtil;
import com.example.boottest.demo.utils.okhttp.OkHttpManager;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Guan
 * @date Created on 2018/11/23
 */
public class GeoUtil {

    /**
     * 高德地图AppKey
     */
    private static final String GAODE_KEY = "a1c533e2ca9edbd9e3d20fef2f7540d2";

    private static class WebUrl {

        /**
         * 地理编码
         */
        public static final String GEO = "https://restapi.amap.com/v3/geocode/geo";
        /**
         * 逆地理编码
         */
        public static final String REGEO = "https://restapi.amap.com/v3/geocode/regeo";

    }

    /**
     * 逆地理编码
     * https://lbs.amap.com/api/webservice/guide/api/georegeo#regeo
     *
     * @param latitude  维度
     * @param longitude 经度
     */
    public static GeoInfo regeo(double latitude, double longitude) {
        return regeo(latitude + "", longitude + "");

    }

    public static GeoInfo regeo(String latitude, String longitude) {
        Map<String, String> params = new HashMap<>();
        params.put("key", GAODE_KEY);
        //经度在前，维度在后
        params.put("location", longitude + "," + latitude);
        params.put("extensions", "all");
        //同步请求方法
        String result = OkHttpManager.getSync(WebUrl.REGEO, params);

        try {
            GeoInfo geoInfo = GsonUtil.fromJson(result, GeoInfo.class);
            return geoInfo;
        } catch (Exception e) {
            return null;
        }


    }


}
