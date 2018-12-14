package com.example.boottest.demo.recommendation.model.params;

import com.example.boottest.demo.recommendation.ctx.ContextConstant;

/**
 * Server向Client SDK发送命令的实体
 * 比如采集情景信息，然后进行应景推送
 *
 * @author Guan
 * @date Created on 2018/12/14
 */
public class ClientCmdParams {

    public String appId;
    public String deviceId;
    public int action;
    public String description;

    public ClientCmdParams(String appId, int action) {
        this.appId = appId;
        this.action = action;
        this.description = ContextConstant.getContextActionDescription(action);
    }


}
