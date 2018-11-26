package com.example.boottest.demo.recommendation.ctx;

import com.example.boottest.demo.recommendation.geo.GeoUtil;
import com.example.boottest.demo.recommendation.model.ContextInfo;
import com.example.boottest.demo.recommendation.model.GeoInfo;
import com.example.boottest.demo.recommendation.model.stats.BaseItem;
import com.example.boottest.demo.recommendation.model.stats.StatsContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Guan
 * @date Created on 2018/11/23
 */
@Service
public class ContextInfoService {


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


    public StatsContext statsContextInfo() {

        StatsContext result = new StatsContext();
        //统计时段
        List<BaseItem> timeSegmentList = contextInfoDao.findContextInfoWithAggregate("timeSegment");
        //统计场所
        List<BaseItem> placeList = contextInfoDao.findContextInfoWithAggregate("locationInfo.placeTypeName");

        result.timeSegmentList = timeSegmentList;
        result.placeList = placeList;

        result.result = true;
        return result;

    }


}
