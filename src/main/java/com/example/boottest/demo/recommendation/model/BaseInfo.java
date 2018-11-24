package com.example.boottest.demo.recommendation.model;

import com.example.boottest.demo.utils.GsonUtil;
import com.google.gson.Gson;

/**
 * @author Guan
 * @date Created on 2018/4/23
 */
public class BaseInfo {


    public String toJson() {
        String json = GsonUtil.toJson(this);
//        String json = new Gson().toJson(this);
        return json;
    }

}
