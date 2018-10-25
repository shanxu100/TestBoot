package com.example.boottest.demo;

import com.example.boottest.demo.netty.NettyConfig;
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
	@Test
	public void contextLoads() {
	    System.out.println(nettyConfig.toString());
	    System.out.println(nettyConfig.equals(nettyConfig2));
	    System.out.println();
	}

}
