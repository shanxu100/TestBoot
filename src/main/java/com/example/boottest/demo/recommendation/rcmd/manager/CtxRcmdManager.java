package com.example.boottest.demo.recommendation.rcmd.manager;

import com.example.boottest.demo.recommendation.ctx.ContextRatingDao;
import com.example.boottest.demo.recommendation.rcmd.GroupLensDataModel2;
import com.example.boottest.demo.utils.LogUtils;
import org.apache.mahout.cf.taste.eval.IRStatistics;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * @author Guan
 * @date Created on 2018/12/12
 */
@Component
public class CtxRcmdManager extends AbstractRcmdManager {

    private static final Logger logger = LoggerFactory.getLogger(CtxRcmdManager.class);


    @Autowired
    private ContextRatingDao contextRatingDao;


    @Override
    @Async
    public DataModel loadDataModel() {
        try {
            logger.info("开始加载DataModel...");
            //记录开始时间，计算耗时
            long startTime = System.currentTimeMillis();
            //准备数据 这里是评分数据。因为数据量不大，所以都在内存了
            List<String> ratingList = contextRatingDao.getContextRating();
            //将数据加载到内存中，GroupLensDataModel是针对开放电影评论数据的
            GroupLensDataModel2 dataModel = new GroupLensDataModel2(ratingList);

            LogUtils.printRunningTime("加载DataModel", startTime);
            return dataModel;

        } catch (IOException e) {
            e.printStackTrace();
            logger.error(logger.getName(), e);
            return null;
        }

    }


}
