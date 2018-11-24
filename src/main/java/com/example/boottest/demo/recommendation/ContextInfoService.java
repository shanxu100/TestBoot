package com.example.boottest.demo.recommendation;

import com.example.boottest.demo.utils.GeoUtil;
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
        contextInfo.locationInfo.formattedAddress = geoInfo.regeocode.formatted_address;
        List<GeoInfo.Aoi> aois = geoInfo.regeocode.aois;
        if (aois != null && aois.size() != 0) {
            contextInfo.locationInfo.aoi = aois.get(0);
        }
        contextInfoDao.addContextInfo(contextInfo);
    }


}
