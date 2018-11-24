package com.example.boottest.demo.recommendation;

import org.apache.mahout.cf.taste.eval.IRStatistics;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 应用协同过滤算法，
 * 1、产生推荐结果；
 * 2、评估模型
 * 3、计算模型信息：准确率、召回率、F-Measure等
 *
 * @author Guan
 * @date Created on 2018/11/7
 */
@RestController
@RequestMapping("/rcmd")
public class RcmdController {

    @Autowired
    RcmdService rcmdService;


    @RequestMapping("/recommend")
    public List<RecommendedItem> userBasedRecommend(@RequestParam long userId, @RequestParam int howMany) {
        return rcmdService.recommend(userId, howMany);
    }

    @RequestMapping("/evaluate")
    public double userBasedEvaluate() {
        return rcmdService.evaluate();
    }

    @RequestMapping("/statistics")
    public IRStatistics statistics() {
        return rcmdService.statistics();
    }

}
