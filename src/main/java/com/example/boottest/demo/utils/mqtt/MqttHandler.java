package com.example.boottest.demo.utils.mqtt;


import android.util.Base64;
import android.util.Log;

import com.google.gson.Gson;
import com.luluteam.pushlib.constant.AppHolder;
import com.luluteam.pushlib.model.PushMessage;
import com.luluteam.pushlib.model.PushReps;
import com.luluteam.pushlib.tools.NotificationHelper;
import com.luluteam.pushlib.tools.Repository;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Guan
 * @date Created on 2017/11/30
 */
public class MqttHandler {
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
        Log.i(TAG, "mqtt on Message: " + json);

        if (StringUtils.isEmpty(json)) {
            return;
        }
        String msg = new String(Base64.decode(json.getBytes(), Base64.DEFAULT));
        Log.i(TAG, "解码后msg==" + msg);
        PushMessage pushMessage = new Gson().fromJson(msg, PushMessage.class);
        sendPushMessageReceive(pushMessage);
        NotificationHelper.sendNotification(AppHolder.appContext, pushMessage);

    }

    /**
     * 接收到消息后，回应服务器，表示“成功接收”
     *
     * @param pushMessage
     */
    private static void sendPushMessageReceive(PushMessage pushMessage) {

        PushReps pushReps = new PushReps();
        pushReps.appId = AppHolder.APP_ID;
        pushReps.deviceId = AppHolder.deviceId;
        pushReps.messageId = pushMessage.getMessageId();
        Repository.postPushReceive(pushReps);
    }


}
