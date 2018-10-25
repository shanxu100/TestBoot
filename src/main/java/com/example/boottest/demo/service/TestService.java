package com.example.boottest.demo.service;

import com.example.boottest.demo.pojo.User;
import org.springframework.stereotype.Service;

/**
 * @author Guan
 * @date Created on 2018/10/23
 */
@Service
public class TestService {


    public User createUser(String username, String pwd) {
        User user = new User(username, pwd);
        return user;
    }

}
