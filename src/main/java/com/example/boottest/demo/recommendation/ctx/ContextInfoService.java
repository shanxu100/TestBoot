package com.example.boottest.demo.recommendation.ctx;

import com.example.boottest.demo.recommendation.geo.GeoUtil;
import com.example.boottest.demo.recommendation.model.ContextInfo;
import com.example.boottest.demo.recommendation.model.GeoInfo;
import com.example.boottest.demo.recommendation.model.ContextRating;
import com.example.boottest.demo.recommendation.model.MsgInfo;
import com.example.boottest.demo.recommendation.model.stats.BaseItem;
import com.example.boottest.demo.recommendation.model.stats.StatsContext;
import com.example.boottest.demo.recommendation.model.stats.StatsMsg;
import com.example.boottest.demo.utils.okhttp.OkHttpManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.rmi.runtime.Log;

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
        GeoInfo geoInfo = GeoUtil.regeo(contextInfo.locationInfo.latitude, contextInfo.locationInfo.longitude);
        List<GeoInfo.Aoi> aois = geoInfo.regeocode.aois;
        if (aois != null && aois.size() != 0) {
            contextInfo.addLocationExtentedInfo(geoInfo.regeocode.formatted_address, aois.get(0).type, aois.get(0).name);
        }
        //补充时段信息
        contextInfo.formatTime();

        //计算隐式评分信息
        if (contextInfo.rating == -1) {
            if (contextInfo.duration != -1 && contextInfo.wordCount != -1.0) {
                double readingRate = ((contextInfo.duration / 1000.0) * ContextConstant.avgReadingSpeed) / contextInfo.wordCount;
                contextInfo.rating = (int) Math.min(readingRate / 0.2, 5.0);
            } else {
                contextInfo.rating = 5;
            }
        }

        //提炼出 User-Item-Rating-Context 的四维信息
        ContextRating contextRating = ContextRating.newInstance(contextInfo);

        //存入数据库的两张表
        contextInfoDao.addContextInfo(contextInfo);
        contextRatingDao.addContextRating(contextRating);
    }


    /**
     * （群体）统计情景信息
     *
     * @return
     */
    public StatsContext statsContextInfo(String appId) {

        StatsContext result = new StatsContext();
        //统计时段
        List<BaseItem> timeSegmentList = contextInfoDao.findContextInfoWithAggregate(appId, "timeSegment");
        //统计场所
        List<BaseItem> placeList = contextInfoDao.findContextInfoWithAggregate(appId, "locationInfo.placeTypeName");

        result.timeSegmentList = timeSegmentList;
        result.placeList = placeList;
        result.result = true;
        logger.info("统计情景信息:appId=" + appId + "\tresult=" + result.toJson());
        return result;

    }

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


}
