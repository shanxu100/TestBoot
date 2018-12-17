package com.example.boottest.demo.utils.mqtt;


import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author Guan
 * @date Created on 2017/11/30
 */
public class MqttHandler {

    private static final Logger logger = LoggerFactory.getLogger(MqttHandler.class);
    private static final String TAG = "MqttHandler";


    private MqttHandler() {

    }

    /**
     * @param connected
     */
    public static void onNetwork(boolean connected) {

    }


    /**
     * 处理mqtt消息
     *
     * @param json
     */
    public static void onMessage(String json) {
        logger.info(TAG, "mqtt on Message: " + json);

        if (StringUtils.isEmpty(json)) {
            return;
        }


    }


}
