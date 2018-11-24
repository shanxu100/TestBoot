package com.example.boottest.demo.recommendation;

import com.example.boottest.demo.utils.GsonUtil;

/**
 * @author Guan
 * @date Created on 2018/4/23
 */
public class BaseInfo {


    public String toJson() {
        String json = GsonUtil.toJson(this);
        return json;
    }

}
