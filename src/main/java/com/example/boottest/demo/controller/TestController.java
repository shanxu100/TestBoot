package com.example.boottest.demo.controller;

import com.example.boottest.demo.netty.NettyConfig;
import com.example.boottest.demo.netty.NettyServer;
import com.example.boottest.demo.pojo.User;
import com.example.boottest.demo.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Guan
 * @date Created on 2018/10/23
 */
@RestController
@RequestMapping("/test")
public class TestController {
    @Autowired
    TestService testService;


    @Autowired
    private NettyServer nettyServer;

    @RequestMapping("/function1")
    public User function1(@RequestParam String username, @RequestParam String pwd) {
        User user = testService.createUser(username, pwd);
        return user;
    }

    @RequestMapping("/function2")
    public void function2(@RequestBody User user) {
        System.out.println(user.toString());
    }

    @RequestMapping("/function3")
    public void function3() {
        nettyServer.stop();
    }

}
