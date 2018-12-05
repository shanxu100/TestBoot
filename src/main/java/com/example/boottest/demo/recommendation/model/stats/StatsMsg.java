package com.example.boottest.demo.recommendation.model.stats;

import com.example.boottest.demo.recommendation.model.BaseInfo;
import com.example.boottest.demo.recommendation.model.MsgInfo;

import java.util.List;

/**
 * @author Guan
 * @date Created on 2018/12/5
 */
public class StatsMsg extends BaseInfo {

    public boolean result;

    public List<BaseItem> msgReadingTimeList;

    public List<MsgInfo> msgInfoList;

    public List<BaseItem> msgOpenCountList;
    public List<BaseItem> msgOpenRatioList;





}
