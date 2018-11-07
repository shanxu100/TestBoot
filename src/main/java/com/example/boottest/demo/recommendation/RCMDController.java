package com.example.boottest.demo.recommendation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Guan
 * @date Created on 2018/11/7
 */
@RestController
@RequestMapping("/rcmd")
public class RCMDController {

    @Autowired
    RCMDService RCMDService;


    @RequestMapping("/userBasedRecommend")
    public void userBasedRecommend(@RequestParam long userId, @RequestParam int howMany) {
        RCMDService.userBasedRecommend(userId, howMany);
    }

}
