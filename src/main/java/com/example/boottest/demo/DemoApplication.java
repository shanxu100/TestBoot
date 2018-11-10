package com.example.boottest.demo;

import com.example.boottest.demo.netty.NettyServer;
import com.example.boottest.demo.recommendation.RcmdManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author Guan
 */
@EnableAsync
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class DemoApplication implements CommandLineRunner {

    private static Logger logger = LoggerFactory.getLogger(DemoApplication.class);


    @Autowired
    NettyServer nettyServer;

    @Autowired
    RcmdManager rcmdManager;


    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
//        nettyServer.start();
        logger.info("command line :"+ Thread.currentThread().toString());
        nettyServer.start();

        rcmdManager.loadDataModel();
    }
}
