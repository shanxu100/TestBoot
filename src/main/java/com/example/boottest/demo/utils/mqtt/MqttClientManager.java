package com.example.boottest.demo.utils.mqtt;


import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
 * @author Guan
 * @date Created on 2018/1/6
 */
public class MqttClientManager {

    private final static int qos = 2;

    private final static Logger logger = LoggerFactory.getLogger(MqttClientManager.class);
    private final static String clientId = "android" + System.currentTimeMillis();
    private final static String TAG = "MqttClientManager";
    /**
     * 订阅的 topic
     */
    private final static String[] topics = new String[]{};

    private MqttClient client;
    private ScheduledExecutorService reconnectExecutor = null;
    private static volatile MqttClientManager manager;

    private static boolean stopped = false;


    private MqttClientManager() {
        try {
            client = new MqttClient(MqttConfig.MQTT_HOST, clientId, new MemoryPersistence());
            client.setCallback(new MyMqttCallback());
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public static MqttClientManager getInstance() {
        if (manager == null) {
            synchronized (MqttClientManager.class) {
                if (manager == null) {
                    manager = new MqttClientManager();
                }
            }
        }
        return manager;
    }


    private MqttConnectOptions getOptions() {
        MqttConnectOptions options = new MqttConnectOptions();
        //设置是否清空session,这里如果设置为false表示服务器会保留客户端的连接记录，这里设置为true表示每次连接到服务器都以新的身份连接
        options.setUserName(MqttConfig.MQTT_USERNAME);
        options.setPassword(MqttConfig.MQTT_PASSWORD.toCharArray());
        options.setCleanSession(true);
        // 设置超时时间 单位为秒
        options.setConnectionTimeout(2);
        // 设置会话心跳时间 单位为秒 服务器会每隔1.5*20秒的时间向客户端发送个消息判断客户端是否在线，但这个方法并没有重连的机制
        options.setKeepAliveInterval(20);
        //自动重连：1-2-4-8-16……最多两分钟-----MqttAndroidClient有用
//        options.setAutomaticReconnect(true);
        return options;
    }


    /**
     * 主要：发送消息
     *
     * @param topic
     * @param message msg
     * @return
     */
    public boolean sendMessage(String topic, MqttMessage message) {
        try {
            if (client.isConnected()) {
                client.publish(topic, message);
                logger.info("mqtt publish: msg=" + message.toString());
                return true;
            } else {
                logger.error("未连接mqtt服务器，发送失败...msg=" + message.toString());
                return false;
            }
        } catch (MqttException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 重连:
     */
    public void reconnect() {
        reconnectExecutor = new ScheduledThreadPoolExecutor(1);
        reconnectExecutor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                if (stopped) {
                    logger.error("mqtt stopped...shutdown reconnectExecutor");
                    reconnectExecutor.shutdownNow();
                    return;
                }
                logger.error("mqtt正在重连...");
                if (!client.isConnected()) {
                    try {
                        client.connect(getOptions());
                    } catch (MqttException e) {
                        logger.error("matt连接失败……");
                    }
                } else {
                    reconnectExecutor.shutdown();
                    logger.debug(TAG, "mqtt关闭重连。。。");
                }
            }
        }, 2, 3, TimeUnit.SECONDS);

    }

    /**
     * 开启
     */
    public void start() {
        stopped = false;
        if (!client.isConnected()) {
            try {
                client.connect(getOptions());
                logger.info("connect to MQTT service");
            } catch (MqttException e) {
                e.printStackTrace();
                MqttHandler.onNetwork(false);
                reconnect();
            }
        }

    }

    /**
     * 结束
     */
    public void stop() {
        stopped = true;
        try {
            if (reconnectExecutor != null && !reconnectExecutor.isShutdown()) {
                logger.error("reconnectExecutor: Shutdown");
                reconnectExecutor.shutdownNow();
            }
            if (client != null) {
                logger.error("client :disconnectForcibly");
                client.disconnectForcibly();
            }
            logger.error("stop MQTT service===");
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    private class MyMqttCallback implements MqttCallbackExtended {

        @Override
        public void connectionLost(Throwable cause) {
            logger.error("mqtt失去连接------");
            MqttHandler.onNetwork(false);
            reconnect();
        }

        @Override
        public void messageArrived(String topic, MqttMessage message) throws Exception {
//            logger.info( "messageArrived--- topic:" + topic + "  message" + message.toString());
            logger.info("messageArrived--- topic:" + topic);
            message.getId();
            message.getQos();
            MqttHandler.onMessage(message.toString());
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken token) {
//            logger.info("deliveryComplete---------" + token.isComplete());
        }

        @Override
        public void connectComplete(boolean reconnect, String serverURI) {
            logger.error("Mqtt连接成功==" + serverURI);
            try {
                //因为是  options.setCleanSession(true); 所以每次断开重连后得重新订阅消息
                client.subscribe(topics);
                MqttHandler.onNetwork(true);
            } catch (MqttException e) {
                e.printStackTrace();
            }

        }
    }


}
