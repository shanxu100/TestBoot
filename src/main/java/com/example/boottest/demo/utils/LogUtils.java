package com.example.boottest.demo.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Guan
 * @date Created on 2018/11/16
 */
public class LogUtils {

    private static final Logger logger = LoggerFactory.getLogger(LogUtils.class);

    public static void printRunningTime(String prefix, long startTime) {
        long stopTime = System.currentTimeMillis();
        logger.info(prefix + "\t开始时间:" + startTime + ",结束时间：" + stopTime +
                ",运行时间：" + (stopTime - startTime) + "ms");
    }

}
