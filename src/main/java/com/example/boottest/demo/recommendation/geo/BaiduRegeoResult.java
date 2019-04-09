package com.example.boottest.demo.recommendation.geo;

import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @author Guan
 * @date Created on 2019/4/9
 */
public class BaiduRegeoResult {
    private int status;
    private Result result;


    public static class Result {
        private List<POI> pois;
    }

    public static class POI {

        private String poiType;
        private String tag;
        private int distance;
    }

    public String toPois() {
        StringBuilder sb = new StringBuilder();
        for (POI poi : result.pois) {
            if (StringUtils.isEmpty(poi.tag)) {
                continue;
            }
            sb.append(poi.tag.split(";")[0]).append("\t")
                    .append(poi.distance).append("\t");
        }
        return sb.toString();
    }
}
