package com.example.boottest.demo.recommendation.ctx;

import com.example.boottest.demo.recommendation.geo.GeoUtil;
import com.example.boottest.demo.recommendation.model.ContextInfo;
import com.example.boottest.demo.recommendation.model.GeoInfo;
import com.example.boottest.demo.recommendation.model.stats.BaseItem;
import com.example.boottest.demo.recommendation.model.stats.StatsContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Guan
 * @date Created on 2018/11/23
 */
@Service
public class ContextInfoService {

    private static final Logger logger = LoggerFactory.getLogger(ContextInfoService.class);


    @Autowired
    ContextInfoDao contextInfoDao;

    public void addContextInfo(ContextInfo contextInfo) {

        /**
         * TODO
         * 1、通过使用地理反向编码，将经纬度信息转换为 场所
         * 2、确定 deviceId-username 的映射
         */
        GeoInfo geoInfo = GeoUtil.regeo(contextInfo.locationInfo.latitude, contextInfo.locationInfo.longitude);
        List<GeoInfo.Aoi> aois = geoInfo.regeocode.aois;
        if (aois != null && aois.size() != 0) {
            contextInfo.addLocationExtentedInfo(geoInfo.regeocode.formatted_address, aois.get(0).type, aois.get(0).name);
        }
        contextInfoDao.addContextInfo(contextInfo);
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


}
