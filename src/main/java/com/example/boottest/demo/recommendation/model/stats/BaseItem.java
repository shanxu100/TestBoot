package com.example.boottest.demo.recommendation.model.stats;

import com.example.boottest.demo.recommendation.model.BaseInfo;

/**
 * @author Guan
 * @date Created on 2018/11/26
 */
public class BaseItem extends BaseInfo {
    public BaseItem() {
    }

    public BaseItem(String x, double y) {
        this.x = x;
        this.y = y;
    }

    public String x;
    public double y;
}
