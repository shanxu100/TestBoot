package com.example.boottest.demo.recommendation.model.stats;

import com.example.boottest.demo.recommendation.model.BaseInfo;

import java.util.List;

/**
 * @author Guan
 * @date Created on 2018/11/26
 */
public class StatsContext extends BaseInfo {

    public boolean result;

    public List<BaseItem> placeList;

    public List<BaseItem> timeSegmentList;

    public List<BaseItem> timeSegmentByHourList;


}
