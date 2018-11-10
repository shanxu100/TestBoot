package com.example.boottest.demo.recommendation;

import org.apache.mahout.cf.taste.eval.IRStatistics;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * @author Guan
 * @date Created on 2018/11/7
 */
@Service
public class RcmdService {

    @Autowired
    UBRCMD ubr;

    @Autowired
    RcmdManager rcmdManager;


    public List<RecommendedItem> recommend(long userId, int howMany) {

        try {
            return rcmdManager.recommend(userId, howMany);
        } catch (Exception e) {
            e.printStackTrace();

            List<RecommendedItem> list = Collections.emptyList();
            return list;
        }

    }

    public double evaluate() {
        try {
            return rcmdManager.evaluate();
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }


    public IRStatistics statistics() {
        try {
            return rcmdManager.statistics();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}
