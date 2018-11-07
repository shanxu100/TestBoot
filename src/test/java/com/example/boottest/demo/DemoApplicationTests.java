package com.example.boottest.demo;

import com.example.boottest.demo.netty.NettyConfig;
import com.example.boottest.demo.recommendation.UBRCMD;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {


    @Autowired
    NettyConfig nettyConfig;
    @Autowired
    NettyConfig nettyConfig2;

    @Autowired
    UBRCMD recommendation;

    @Test
    public void contextLoads() {
        try {
//            recommendation.recommend(5, 5);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
