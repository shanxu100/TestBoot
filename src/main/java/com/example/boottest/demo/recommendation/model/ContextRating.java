package com.example.boottest.demo.recommendation.model;

import com.example.boottest.demo.recommendation.ctx.ContextConstant;

/**
 * 用于记录 User-Item-Rating-Context 的四维信息
 * @author Guan
 * @date Created on 2018/12/5
 */
public class ContextRating extends BaseInfo {

    public String deviceId;

    public String itemId;
    public int rating;

    public String contextId;

    public static ContextRating newInstance(ContextInfo contextInfo) {
        ContextRating contextRating = new ContextRating();
        contextRating.deviceId = contextInfo.deviceId;
        contextRating.itemId = contextInfo.itemId;
        contextRating.rating = contextInfo.rating;
        contextRating.contextId = contextInfo.getContextId();
        return contextRating;
    }

}
