package com.example.boottest.demo.recommendation.offline.math;

import java.text.SimpleDateFormat;

/**
 * @author Guan
 * @date Created on 2019/3/13
 */
public class TimeUtil {
    private static SimpleDateFormat f = new SimpleDateFormat("HH");

    public static int getTimeSegment(long timestamp) {
        // 获取小时
        String sTime = f.format(timestamp);
        int hour = Integer.parseInt(sTime);
        if (hour > 6 && hour <= 12) {
            // 上午
            return 1;
        } else if (hour > 12 && hour <= 18) {
            // 下午
            return 2;
        } else {
            //晚上
            return 3;
        }
    }
}
