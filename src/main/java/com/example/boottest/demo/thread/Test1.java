package com.example.boottest.demo.thread;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Guan
 * @date Created on 2019/3/13
 */
public class Test1 {

    public static void main(String[] args) {

        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
        executorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                System.out.println("do something" + Thread.currentThread().getName());
//                Thread.sleep();

            }
        }, 2000, 1000, TimeUnit.MILLISECONDS);

    }
}
