package com.example.boottest.demo.recommendation.ctx;

import com.example.boottest.demo.recommendation.geo.GeoUtil;
import com.example.boottest.demo.recommendation.model.ContextInfo;
import com.example.boottest.demo.recommendation.model.GeoInfo;
import com.example.boottest.demo.recommendation.model.ContextRating;
import com.example.boottest.demo.recommendation.model.params.ClientCmdParams;
import com.example.boottest.demo.recommendation.model.params.PersonalizedPushParams;
import com.example.boottest.demo.recommendation.model.stats.MsgInfo;
import com.example.boottest.demo.recommendation.model.stats.BaseItem;
import com.example.boottest.demo.recommendation.model.stats.StatsContext;
import com.example.boottest.demo.recommendation.model.stats.StatsMsg;
import com.example.boottest.demo.utils.Base64Util;
import com.example.boottest.demo.utils.mqtt.MqttSender;
import com.example.boottest.demo.utils.okhttp.OkHttpManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.*;

/**
 * @author Guan
 * @date Created on 2018/11/23
 */
@Service
public class ContextInfoService {

    private static final Logger logger = LoggerFactory.getLogger(ContextInfoService.class);

    private static final Comparator COMPARATOR = new Comparator<BaseItem>() {
        @Override
        public int compare(BaseItem o1, BaseItem o2) {
            if (o1.y > o2.y) {
                return -1;
            } else if (o1.y < o2.y) {
                return 1;
            } else {
                return 0;
            }
        }
    };

    @Autowired
    ContextInfoDao contextInfoDao;

    @Autowired
    ContextRatingDao contextRatingDao;

    public void addContextInfo(ContextInfo contextInfo) {

        /**
         * TODO
         * 1、通过使用地理反向编码，将经纬度信息转换为 场所
         * 2、确定 deviceId-username 的映射
         * 3、隐式评分：根据Message的字数和duration的长短进行比较。参考普通人的平均阅读次数
         */
        //补充场所信息
        if (contextInfo.locationInfo.latitude == -1 || contextInfo.locationInfo.longitude == -1) {
            contextInfo.addUnknownLocationExtentedInfo();
        } else {
            GeoInfo geoInfo = GeoUtil.regeo(contextInfo.locationInfo.latitude, contextInfo.locationInfo.longitude);
            List<GeoInfo.Aoi> aois = geoInfo.regeocode.aois;
            if (aois != null && aois.size() != 0) {
                contextInfo.addLocationExtentedInfo(geoInfo.regeocode.formatted_address, aois.get(0).type, aois.get(0).name);
            }
        }

        //补充时段信息
        contextInfo.formatTime();

        ContextInfo.Optional optional = contextInfo.optional;

        if (optional != null && optional.action == ContextConstant.ACTION_MSG) {
            //计算隐式评分信息
            if (optional.userBehaviorInfo.rating == -1) {
                if (optional.userBehaviorInfo.duration != -1
                        && optional.userBehaviorInfo.wordCount != -1.0) {
                    double readingRate = ((optional.userBehaviorInfo.duration / 1000.0) * ContextConstant.avgReadingSpeed) / optional.userBehaviorInfo.wordCount;
                    optional.userBehaviorInfo.rating = (int) Math.min(readingRate / 0.2, 5.0);
                } else {
                    optional.userBehaviorInfo.rating = 5;
                }
            }
            //提炼出 User-Item-Rating-Context 的四维信息,并存入数据库
            ContextRating contextRating = ContextRating.newInstance(contextInfo);
            contextRatingDao.addContextRating(contextRating);
        } else if (optional != null
                && optional.action == ContextConstant.ACTION_CONTEXT) {
            // TODO 进行应景推送：获取推荐列表，然后推送消息

        }

        //存入数据库
        contextInfoDao.addContextInfo(contextInfo);


    }


    /**
     * 进行个性化推送
     *
     * @param params
     */
    public void personalizedPush(PersonalizedPushParams params) {
        ClientCmdParams clientCmdParams = new ClientCmdParams(params.appId, ContextConstant.ACTION_CONTEXT);
        //TODO 调用mqtt的接口，开始向客户端发起请求
        //用户收到这个请求后开始采集情景数据，然后通过接口“addContextInfo()”上报服务器。服务器保存信息后再进行应景推送
        MqttSender.sendMessage(params.appId, Base64Util.getBase64(clientCmdParams.toJson()));

    }


    /**
     * （群体）统计情景信息
     *
     * @return
     */
    public StatsContext statsContextInfo(String appId) {

        StatsContext result = new StatsContext();
        //统计场所
        List<BaseItem> placeList = contextInfoDao.findContextInfoWithAggregate(appId, "locationInfo.placeTypeName");
        //统计时段(概览)
        List<BaseItem> timeSegmentList = contextInfoDao.findContextInfoWithAggregate(appId, "timeSegment");
        //统计时段（按小时）
        List<BaseItem> timeSegmentByHourList = contextInfoDao.findContextInfoWithAggregate(appId, "hour");
        List<BaseItem> timeSegmentByHourList2 = new ArrayList<>(24);
        for (int i = 0; i < 24; i++) {
            timeSegmentByHourList2.add(new BaseItem(i + "", 0));
        }
        for (BaseItem baseItem : timeSegmentByHourList) {
            int x = Integer.parseInt(baseItem.x);
            timeSegmentByHourList2.set(x, baseItem);
        }
        for (BaseItem baseItem : timeSegmentByHourList2) {
            String x = baseItem.x;
            baseItem.x = x + " 时";
        }

        result.timeSegmentList = timeSegmentList;
        result.timeSegmentByHourList = timeSegmentByHourList2;
        result.placeList = placeList;
        result.result = true;
        logger.info("统计情景信息:appId=" + appId + "\tresult=" + result.toJson());
        return result;

    }

    /**
     * 统计消息阅读情况
     *
     * @param appId
     * @return
     */
    public StatsMsg statsMsgInfo(String appId) {
        StatsMsg result = new StatsMsg();
        List<BaseItem> msgReadingTimeList = contextInfoDao.findMsgReadingDurationWithAggregate(appId);

        //Msg的阅读时间，并排序
        result.msgReadingTimeList = msgReadingTimeList;

        //获取Message信息
        Map<String, String> map = new HashMap<>();
        map.put("appId", appId);
        String msgInfoJson = OkHttpManager.getSync("http://i-test.com.cn/PerformanceMonitorCenter/getPushMessageList", map);
        Type type = new TypeToken<List<MsgInfo>>() {
        }.getType();
        List<MsgInfo> msgInfoList = new Gson().fromJson(msgInfoJson, type);
        List<BaseItem> msgOpenCountList = new ArrayList<>(msgInfoList.size());
        List<BaseItem> msgOpenRatioList = new ArrayList<>(msgInfoList.size());

        for (MsgInfo msgInfo : msgInfoList) {

            //记录“打开次数”的list
            msgOpenCountList.add(new BaseItem(msgInfo.messageId, msgInfo.openCount));

            //记录“打开率”的list
            double ratio = 0;
            msgInfo.totalCount = msgInfo.openCount > msgInfo.totalCount ? msgInfo.openCount : msgInfo.totalCount;
            if (msgInfo.totalCount != 0) {
                ratio = msgInfo.openCount / (msgInfo.totalCount + 0.0);
            }
            msgOpenRatioList.add(new BaseItem(msgInfo.messageId, ratio));

        }
        msgOpenCountList.sort(COMPARATOR);
        msgOpenRatioList.sort(COMPARATOR);


        result.msgInfoList = msgInfoList;
        result.msgOpenCountList = msgOpenCountList;
        result.msgOpenRatioList = msgOpenRatioList;
        result.result = true;

        return result;

    }



    //=====================================================
    //测试
    //=====================================================
    @Autowired
    MongoTemplate mongoTemplate;

    /**
     * 添加测试用的数据
     */
    public void addTestRecord() {
        FindIterable<Document> findIterable = mongoTemplate.getCollection("PushMessage").find();
        Iterator<Document> iterator = findIterable.iterator();
        Gson gson = new Gson();
        Set<String> set = new HashSet<>();
        while (iterator.hasNext()) {
            String json = iterator.next().toJson();
            MyClass c = gson.fromJson(json, MyClass.class);
            set.add(c.messageId);
        }

        MongoCollection<Document> collection = mongoTemplate.getCollection("PushRating");

        for (String string : set) {
            ContextRating rating = new ContextRating();
            //"ffffffff-84db-c9d2-0000-000067cadb8d"
            //"00000000-4a22-4437-ffff-ffff8a0e4c7a"
            //00000000-790a-45b0-ffff-ffffbe68e73d
            rating.deviceId = "00000000-49f7-fdb7-ffff-ffffa25fd887";
            rating.messageId = string;
            rating.initItemId();
            rating.initUserId();
            rating.rating = (int) (Math.random() * 5);
            rating.contextId = (int) (Math.random() * 35) + "";
            Document document = Document.parse(rating.toJson());
            collection.insertOne(document);

        }
        for (String string : set) {
            ContextRating rating = new ContextRating();
            //"00000000-4a22-4437-ffff-ffff8a0e4c7a"
            //00000000-790a-45b0-ffff-ffffbe68e73d
            rating.deviceId = "ffffffff-84db-c9d2-0000-000067cadb8d";
            rating.messageId = string;
            rating.initItemId();
            rating.initUserId();
            rating.rating = (int) (Math.random() * 5);
            rating.contextId = (int) (Math.random() * 35) + "";
            Document document = Document.parse(rating.toJson());
            collection.insertOne(document);

        }

        for (String string : set) {
            ContextRating rating = new ContextRating();
            //00000000-790a-45b0-ffff-ffffbe68e73d
            rating.deviceId = "00000000-4a22-4437-ffff-ffff8a0e4c7a";
            rating.messageId = string;
            rating.initItemId();
            rating.initUserId();
            rating.rating = (int) (Math.random() * 5);
            rating.contextId = (int) (Math.random() * 35) + "";
            Document document = Document.parse(rating.toJson());
            collection.insertOne(document);
        }

        for (String string : set) {
            ContextRating rating = new ContextRating();
            rating.deviceId = "00000000-790a-45b0-ffff-ffffbe68e73d";
            rating.messageId = string;
            rating.initItemId();
            rating.initUserId();
            rating.rating = (int) (Math.random() * 5);
            rating.contextId = (int) (Math.random() * 35) + "";
            Document document = Document.parse(rating.toJson());
            collection.insertOne(document);
        }
    }

    public static class MyClass{
        public String messageId;

    }

}
