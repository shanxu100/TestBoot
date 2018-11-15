package com.example.boottest.demo.controller;

import com.example.boottest.demo.netty.NettyServer;
import com.example.boottest.demo.pojo.User;
import com.example.boottest.demo.service.TestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.util.function.Tuples;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Guan
 * @date Created on 2018/10/23
 */
@Controller
@RequestMapping("/test")
public class TestController {
    @Autowired
    TestService testService;
    private static final Logger logger= LoggerFactory.getLogger(TestController.class);


    @Autowired
    private NettyServer nettyServer;

    @RequestMapping("/function1")
    @ResponseBody
    public User function1(@RequestParam String username, @RequestParam String pwd) {
        User user = testService.createUser(username, pwd);
        return user;
    }

    @RequestMapping("/function2")
    @ResponseBody
    public void function2(@RequestBody User user) {
        System.out.println(user.toString());
    }

    @RequestMapping("/function3")
    public String function3(Map<String,String> map) {
        map.put("welcome","balabala");
        return "testPage";
    }




}
