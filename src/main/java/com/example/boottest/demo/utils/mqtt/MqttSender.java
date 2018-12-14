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
     * @param cardId
     * @param json
     * @return
     */
    public static boolean sendMessage(String cardId, String json) {
        MqttMessage message = new MqttMessage();
        message.setQos(2);
        message.setPayload(json.getBytes());
        return MqttClientManager.getInstance().sendMessage(cardId, message);
    }


    /**
     * 发送数据：json格式的String数据
     *
     * @param topic
     * @param json
     * @param retained true：如果客户端不在线，在重新连线后，发送保留的消息
     * @return
     */
    public static boolean sendMessage(String topic, String json, boolean retained) {
        MqttMessage message = new MqttMessage();
        message.setQos(2);
        message.setPayload(json.getBytes());
        message.setRetained(retained);
        return MqttClientManager.getInstance().sendMessage(topic, message);
    }

}
