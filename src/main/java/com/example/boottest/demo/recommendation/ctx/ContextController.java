package com.example.boottest.demo.recommendation.ctx;

import com.example.boottest.demo.recommendation.model.ContextInfo;
import com.example.boottest.demo.recommendation.model.stats.StatsContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Guan
 * @date Created on 2018/11/23
 */
@RestController
@RequestMapping("/context")
public class ContextController {

    @Autowired
    ContextInfoService contextInfoService;


    @RequestMapping("/addContextInfo")
    public void addContextInfo(@RequestBody ContextInfo contextInfo) {
        contextInfoService.addContextInfo(contextInfo);
    }


    @RequestMapping("/statsContextInfo")
    public StatsContext statsContextInfo() {
        return contextInfoService.statsContextInfo();
    }

}
