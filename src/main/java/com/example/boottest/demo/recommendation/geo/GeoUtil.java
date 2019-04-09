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
    private static final String GAODE_KEY_HAIWAI = "9356ac15138da6b3a2811e8ba2656f7b";
    private static final String GAODE_KEY_HAIWAI_BAIDU = "XUZBDAjOeIA2S2aKHQ4M2biT4nlTiGIu";

    private static class WebUrl {

        /**
         * 地理编码
         */
        public static final String GEO = "https://restapi.amap.com/v3/geocode/geo";
        /**
         * 逆地理编码
         */
        public static final String REGEO = "https://restapi.amap.com/v3/geocode/regeo";
        public static final String REGEO_BAIDU = "http://api.map.baidu.com/geocoder/v2/";

        public static final String FIND_POI = "https://restapi.amap.com/v3/place/text";

    }

    /**
     * 逆地理编码
     * https://lbs.amap.com/api/webservice/guide/api/georegeo#regeo
     *
     * @param latitude  纬度
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

    /**
     * 使用百度地图进行海外逆地理编码
     *
     * @param latitude
     * @param longitude
     */
    public static BaiduRegeoResult regeoBaidu(String latitude, String longitude) {
        Map<String, String> params = new HashMap<>();
        params.put("ak", GAODE_KEY_HAIWAI_BAIDU);
        //经度在前，维度在后
        params.put("location", latitude + "," + longitude);
        params.put("output", "json");
        params.put("radius", "1000");
        params.put("pois", "1");
        params.put("coordtype", "wgs84ll");
        params.put("ret_coordtype", "wgs84ll");
        //同步请求方法
        String result = OkHttpManager.getSync(WebUrl.REGEO_BAIDU, params);
        BaiduRegeoResult result1 = GsonUtil.fromJson(result, BaiduRegeoResult.class);
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result1;

    }

    public static GeoInfo findPOI(String latitude, String longitude) {
        Map<String, String> params = new HashMap<>();
        params.put("key", GAODE_KEY_HAIWAI);
        //经度在前，维度在后
        params.put("location", longitude + "," + latitude);
        //同步请求方法
        String result = OkHttpManager.getSync(WebUrl.FIND_POI, params);

        try {
            GeoInfo geoInfo = GsonUtil.fromJson(result, GeoInfo.class);
            return geoInfo;
        } catch (Exception e) {
            return null;
        }


    }

    public static void main(String[] args) {

        String s = "22.22314\t-159.493565";
        String[] ss = s.split("\t");
        regeoBaidu(ss[0], ss[1]);

    }


}
