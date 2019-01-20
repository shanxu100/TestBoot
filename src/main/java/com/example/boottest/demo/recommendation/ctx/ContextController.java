package com.example.boottest.demo.recommendation.ctx;

import com.example.boottest.demo.recommendation.model.ContextInfo;
import com.example.boottest.demo.recommendation.model.params.PersonalizedPushParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;

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

    @RequestMapping("/personalizedPush")
    public void personalizedPush(@RequestBody PersonalizedPushParams params) {
        contextInfoService.personalizedPush(params);
    }

    @RequestMapping("/statsContextInfo")
    public void statsContextInfo(HttpServletRequest request, HttpServletResponse response) {
        try {
            request.setCharacterEncoding("UTF-8");

            String appId = request.getParameter("appId");

            response.setContentType("application/json;charset=UTF-8");
            response.setHeader("Access-Control-Allow-Credentials", "true");
            response.setHeader("Access-Control-Allow-Headers", "Origin, No-Cache, X-Requested-With, If-Modified-Since, Pragma, Last-Modified, Cache-Control, Expires, Content-Type, X-E4M-With");
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.getWriter().print(contextInfoService.statsContextInfo(appId).toJson());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @RequestMapping("/statsMsgInfo")
    public void statsMsgInfo(HttpServletRequest request, HttpServletResponse response) {
        try {
            request.setCharacterEncoding("UTF-8");
            String appId = request.getParameter("appId");

            response.setContentType("application/json;charset=UTF-8");
            response.setHeader("Access-Control-Allow-Credentials", "true");
            response.setHeader("Access-Control-Allow-Headers", "Origin, No-Cache, X-Requested-With, If-Modified-Since, Pragma, Last-Modified, Cache-Control, Expires, Content-Type, X-E4M-With");
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.getWriter().print(contextInfoService.statsMsgInfo(appId).toJson());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("/testAddRecord")
    public void testAddRecord(){
        contextInfoService.addTestRecord();
    }
}
