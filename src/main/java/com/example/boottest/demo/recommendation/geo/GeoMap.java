package com.example.boottest.demo.recommendation.geo;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Guan
 * @date Created on 2018/11/24
 */
public class GeoMap {

    /**
     * 将高德地图的20多种分类简化为12种
     * <p>
     * (01)机动车服务：汽车服务(010000)、汽车销售(020000)、汽车维修(030000)、摩托车维修(040000)
     * <p>
     * (02)餐饮购物：餐饮服务(050000)、购物服务(060000)、
     * <p>
     * (03)公共生活：生活服务(070000)
     * <p>
     * (04)体育休闲：体育休闲服务(080000)
     * <p>
     * (05)医疗保健：医疗保健服务(090000)
     * <p>
     * (06)旅游住宿：住宿服务(100000)、风景名胜(110000)
     * <p>
     * (07)商务住宅：商务住宅(120000)
     * <p>
     * (08)政府社团：政府机构及社会团体(130000)
     * <p>
     * (09)科教文化：科教文化服务(140000)
     * <p>
     * (10)交通服务：交通设施服务(150000)
     * <p>
     * (11)金融保险：金融保险服务(160000)
     * <p>
     * (12)公司企业：公司企业(170000)
     * <p>
     * (13)其他：道路附属设施(180000)，地名地址信息(190000)、公共设施(200000)、事件活动(220000)、通行设施(990000)、室内设施(970000)
     */
    private static final ConcurrentHashMap<String, String> POI_TYPE_MAP = new ConcurrentHashMap<>();


//    private static final



    static {
        POI_TYPE_MAP.put("010000", "01");
        POI_TYPE_MAP.put("020000", "01");
        POI_TYPE_MAP.put("030000", "01");
        POI_TYPE_MAP.put("040000", "01");

        POI_TYPE_MAP.put("050000", "02");
        POI_TYPE_MAP.put("060000", "02");

        POI_TYPE_MAP.put("070000", "03");

        POI_TYPE_MAP.put("080000", "04");

        POI_TYPE_MAP.put("090000", "05");

        POI_TYPE_MAP.put("100000", "06");
        POI_TYPE_MAP.put("110000", "06");

        POI_TYPE_MAP.put("120000", "07");

        POI_TYPE_MAP.put("130000", "08");

        POI_TYPE_MAP.put("140000", "09");

        POI_TYPE_MAP.put("150000", "10");

        POI_TYPE_MAP.put("160000", "11");

        POI_TYPE_MAP.put("170000", "12");

        POI_TYPE_MAP.put("180000", "13");
        POI_TYPE_MAP.put("190000", "13");
        POI_TYPE_MAP.put("200000", "13");
        POI_TYPE_MAP.put("220000", "13");
        POI_TYPE_MAP.put("970000", "13");
        POI_TYPE_MAP.put("990000", "13");

    }

    /**
     * 根据高德地图的POI分类编码，获取场所Id
     *
     * @param poiType
     * @return
     */
    public static String getPlaceType(String poiType) {
        String key = poiType.substring(0, 2) + "0000";
        return POI_TYPE_MAP.get(key);
    }
    public static String getPlaceTypeName(String placeType) {
        return "";
    }

}
