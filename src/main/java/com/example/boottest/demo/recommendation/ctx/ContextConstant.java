package com.example.boottest.demo.recommendation.ctx;

import com.example.boottest.demo.recommendation.model.ContextInfo;
import org.springframework.util.StringUtils;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 场所相关：根据高德地图的POI分类，简化为13种场所
 * 时段相关：共 上午、下午、晚上 三个值
 * <p>
 * 场所 x 时段 = 13 * 3 = 39 ，即Context可以抽象成39个离散值
 *
 * @author Guan
 * @date Created on 2018/11/24
 */
public class ContextConstant {

    /**
     * 将高德地图的20多种分类简化为12种
     * <p>
     * (01)机动车服务：汽车服务(010000)、汽车销售(020000)、汽车维修(030000)、摩托车维修(040000)
     * <p>
     * (02)餐饮购物：餐饮服务(050000)、购物服务(060000)、
     * <p>
     * (03)生活服务：生活服务(070000)
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
    private static final ConcurrentHashMap<String, Place> POI_TYPE_MAP = new ConcurrentHashMap<>();

    private static final ConcurrentHashMap<String, String> TIME_SEGMENT_MAP = new ConcurrentHashMap<>();


    private static final ConcurrentHashMap<Integer, String> CONTEXT_ACTION = new ConcurrentHashMap<>();


    /**
     * 平均阅读速度：10 个/秒
     */
    public static final int avgReadingSpeed = 10;


    /**
     * 默认:只有情景信息
     */
    public static final int ACTION_NORMAL = 0;

    /**
     * client上报情景信息，同时服务器需要经行 应景推送
     */
    public static final int ACTION_CONTEXT = 1;

    /**
     * client在阅读Msg的时候上报的情景信息
     */
    public static final int ACTION_MSG = 2;


    static {
        POI_TYPE_MAP.put("010000", new Place("01", "机动车服务"));
        POI_TYPE_MAP.put("020000", new Place("01", "机动车服务"));
        POI_TYPE_MAP.put("030000", new Place("01", "机动车服务"));
        POI_TYPE_MAP.put("040000", new Place("01", "机动车服务"));

        POI_TYPE_MAP.put("050000", new Place("02", "餐饮购物"));
        POI_TYPE_MAP.put("060000", new Place("02", "餐饮购物"));

        POI_TYPE_MAP.put("070000", new Place("03", "生活服务"));

        POI_TYPE_MAP.put("080000", new Place("04", "体育休闲"));

        POI_TYPE_MAP.put("090000", new Place("05", "医疗保健"));

        POI_TYPE_MAP.put("100000", new Place("06", "旅游住宿"));
        POI_TYPE_MAP.put("110000", new Place("06", "旅游住宿"));

        POI_TYPE_MAP.put("120000", new Place("07", "商务住宅"));

        POI_TYPE_MAP.put("130000", new Place("08", "政府社团"));

        POI_TYPE_MAP.put("140000", new Place("09", "科教文化"));

        POI_TYPE_MAP.put("150000", new Place("10", "交通服务"));

        POI_TYPE_MAP.put("160000", new Place("11", "金融保险"));

        POI_TYPE_MAP.put("170000", new Place("12", "公司企业"));

        POI_TYPE_MAP.put("180000", new Place("13", "其他"));
        POI_TYPE_MAP.put("190000", new Place("13", "其他"));
        POI_TYPE_MAP.put("200000", new Place("13", "其他"));
        POI_TYPE_MAP.put("220000", new Place("13", "其他"));
        POI_TYPE_MAP.put("970000", new Place("13", "其他"));
        POI_TYPE_MAP.put("990000", new Place("13", "其他"));
        POI_TYPE_MAP.put("000000", new Place("13", "其他"));

        TIME_SEGMENT_MAP.put("上午", "01");
        TIME_SEGMENT_MAP.put("下午", "02");
        TIME_SEGMENT_MAP.put("晚上", "03");

        CONTEXT_ACTION.put(ACTION_NORMAL, "Client主动上报情景信息");
        CONTEXT_ACTION.put(ACTION_CONTEXT, "Client响应Server请求，上报当前情景信息，开展应景推送");
        CONTEXT_ACTION.put(ACTION_MSG, "阅读Msg的时候上报的情景信息");


    }

    /**
     * 根据高德地图的POI分类编码，获取场所Id
     *
     * @param poiType 从高德地图API中获取的地理编码
     * @return placeType 简化后的地理编码
     */
    public static String getPlaceType(String poiType) {
        String key = "990000";
        if (!StringUtils.isEmpty(poiType) && poiType.length() == 6) {
            key = poiType.substring(0, 2) + "0000";
        }
        return POI_TYPE_MAP.get(key).type;
    }

    /**
     * 根据高德地图的POI分类编码，获取 场所 对应的中文描述
     *
     * @param poiType 从高德地图API中获取的地理编码
     * @return placeTypeName 简化后的地理编码的中文描述
     */
    public static String getPlaceTypeName(String poiType) {
        String key = "990000";
        if (!StringUtils.isEmpty(poiType) && poiType.length() == 6) {
            key = poiType.substring(0, 2) + "0000";
        }
        return POI_TYPE_MAP.get(key).TypeName;
    }

    /**
     * @param timeSegment
     * @return
     */
    public static String getTimeSegmentId(String timeSegment) {
        String defaultTimeSegment = "01";
        if (TIME_SEGMENT_MAP.containsKey(timeSegment)) {
            return TIME_SEGMENT_MAP.get(timeSegment);
        }
        return defaultTimeSegment;
    }


    /**
     * 找到对应的情景Id，即ContextId
     *
     * @param contextInfo
     * @return
     */
    public static String getContextId(ContextInfo contextInfo) {
        String placeType = getPlaceType(contextInfo.locationInfo.poiType);
        String timeSegmentId = getTimeSegmentId(contextInfo.timeSegment);
        return getContextId(placeType, timeSegmentId);
    }

    /**
     * 根据 场所Id和时间段Id，计算出所对应的情景Id，即ContextId
     *
     * @param placeType
     * @param timeSegmentId
     * @return
     */
    public static String getContextId(String placeType, String timeSegmentId) {
        placeType = StringUtils.isEmpty(placeType) ? "01" : placeType;
        timeSegmentId = StringUtils.isEmpty(timeSegmentId) ? "01" : timeSegmentId;
        int contextId = (Integer.parseInt(placeType) - 1) * 3 + Integer.parseInt(timeSegmentId);
        return contextId + "";
    }


    /**
     * @param contextAction
     * @return
     */
    public static String getContextActionDescription(int contextAction) {
        if (CONTEXT_ACTION.containsKey(contextAction)) {
            return CONTEXT_ACTION.get(contextAction);
        }
        return "未知：Context Action=" + contextAction;
    }


    //=====================================================

    private static class Place {
        //即Id
        public String type;
        public String TypeName;

        public Place(String type, String typeName) {
            this.type = type;
            TypeName = typeName;
        }
    }

}
