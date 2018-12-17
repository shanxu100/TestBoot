package com.example.boottest.demo.utils.mqtt;


import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * @author Guan
 * @date Created on 2017/12/12
 */
public class MqttSender {




    /**
     * 发送数据：json格式的String数据
     *
     * @param topic
     * @param json
     * @return
     */
    public static boolean sendMessage(String topic, String json) {
        MqttMessage message = new MqttMessage();
        message.setQos(2);
        message.setPayload(json.getBytes());
        return MqttClientManager.getInstance().sendMessage(topic, message);
    }

}
