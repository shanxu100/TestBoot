package com.example.boottest.demo.recommendation.rcmd;

import com.example.boottest.demo.utils.RedisClient;
import org.apache.mahout.cf.taste.eval.IRStatistics;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    RcmdManager rcmdManager;

    @Autowired
    @Qualifier("datasetPath")
    private String datasetPath;

    private static final Logger logger = LoggerFactory.getLogger(RcmdService.class);


    public List<RecommendedItem> recommend(long userId, int howMany) {

        try {
            return rcmdManager.recommend(userId, howMany);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(logger.getName(), e);

            List<RecommendedItem> list = Collections.emptyList();
            return list;
        }

    }

    public double evaluate() {

        //获取redis的缓存
        String redisKey = datasetPath + "_evaluate";
        logger.info("评估：redisKey={}", redisKey);

        if (RedisClient.exist(redisKey)) {
            return Double.parseDouble(RedisClient.get(redisKey).toString());
        }

        try {
            double score = rcmdManager.evaluate();
            //将结果存入redis
            if (score >= 0) {
                RedisClient.set(redisKey, score + "");
            }
            return score;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(logger.getName(), e);
            return -1;
        }
    }


    public IRStatistics statistics() {
        String redisKey = datasetPath + "_statistics";
        logger.info("统计：redisKey={}", redisKey);
        if (RedisClient.exist(redisKey)) {
            return RedisClient.get(redisKey, IRStatistics.class);
        }

        try {
            IRStatistics statistics = rcmdManager.statistics();
            if (statistics != null) {
                RedisClient.set(redisKey, statistics);
            }
            return statistics;
        } catch (Exception e) {

            e.printStackTrace();
            logger.error(logger.getName(), e);
            return null;
        }
    }


}
