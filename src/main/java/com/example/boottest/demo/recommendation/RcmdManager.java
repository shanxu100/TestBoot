package com.example.boottest.demo.recommendation;

import com.example.boottest.demo.utils.LogUtils;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.eval.IRStatistics;
import org.apache.mahout.cf.taste.eval.RecommenderBuilder;
import org.apache.mahout.cf.taste.eval.RecommenderEvaluator;
import org.apache.mahout.cf.taste.eval.RecommenderIRStatsEvaluator;
import org.apache.mahout.cf.taste.impl.eval.AverageAbsoluteDifferenceRecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.eval.GenericRecommenderIRStatsEvaluator;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.apache.mahout.cf.taste.similarity.precompute.example.GroupLensDataModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * @author Guan
 * @date Created on 2018/11/9
 */
@Component
public class RcmdManager {
    private static final Logger logger = LoggerFactory.getLogger(RcmdManager.class);


    @Autowired
    @Qualifier("datasetPath")
    private String datasetPath;


    private DataModel dataModel;


    public DataModel getDataModel() {
        return dataModel;
    }


    @Async
    public void loadDataModel() {
        try {
            logger.info("开始加载DataModel...");
            //记录开始时间，计算耗时
            long startTime = System.currentTimeMillis();
            //准备数据 这里是电影评分数据
            File file = new File(datasetPath);
            //将数据加载到内存中，GroupLensDataModel是针对开放电影评论数据的
            dataModel = new GroupLensDataModel(file);

            LogUtils.printRunningTime("加载DataModel", startTime);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    /**
     * 产生推荐
     *
     * @param userId
     * @param howMany
     * @return
     * @throws Exception
     */
    public List<RecommendedItem> recommend(long userId, int howMany) throws Exception {
        if (dataModel == null) {
            return Collections.emptyList();
        }
        long startTime = System.currentTimeMillis();
        //计算相似度，相似度算法有很多种，欧几里得、皮尔逊等等。
        UserSimilarity similarity = new PearsonCorrelationSimilarity(dataModel);
        //计算最近邻域，邻居有两种算法，基于固定数量的邻居和基于相似度的邻居，这里使用基于固定数量的邻居
        UserNeighborhood userNeighborhood = new NearestNUserNeighborhood(100, similarity, dataModel);
        //构建推荐器，协同过滤推荐有两种，分别是基于用户的和基于物品的，这里使用基于用户的协同过滤推荐
        Recommender recommender = new GenericUserBasedRecommender(dataModel, userNeighborhood, similarity);

        //给用户ID等于5的用户推荐10部电影
        List<RecommendedItem> recommendedItemList = recommender.recommend(userId, howMany);

        //打印推荐的结果
        LogUtils.printRunningTime("使用基于用户的协同过滤算法,为用户:" + userId + "推荐 " + howMany + " 个商品。", startTime);
        for (RecommendedItem recommendedItem : recommendedItemList) {
            logger.error(recommendedItem.toString());
        }
        return recommendedItemList;

    }

    /**
     * 评估模型
     *
     * @return
     * @throws Exception
     */
    public double evaluate() throws Exception {
        if (dataModel == null) {
            return -1;
        }

        long startTime = System.currentTimeMillis();

        //推荐评估，使用均方根
        //RecommenderEvaluator evaluator = new RMSRecommenderEvaluator();
        //推荐评估，使用平均差值
        RecommenderEvaluator evaluator = new AverageAbsoluteDifferenceRecommenderEvaluator();
        RecommenderBuilder builder = new RecommenderBuilder() {
            @Override
            public Recommender buildRecommender(DataModel dataModel) throws TasteException {
                UserSimilarity similarity = new PearsonCorrelationSimilarity(dataModel);
                UserNeighborhood neighborhood = new NearestNUserNeighborhood(2, similarity, dataModel);
                return new GenericUserBasedRecommender(dataModel, neighborhood, similarity);
            }
        };
        // 用70%的数据用作训练，剩下的30%用来测试.最后得出的评估值越小，说明推荐结果越好
        double score = evaluator.evaluate(builder, null, dataModel, 0.7, 1.0);

        LogUtils.printRunningTime("模型评估，score=" + score, startTime);
        return score;
    }

    /**
     * 计算准确率召回率
     * TODO 未完成
     *
     * @throws Exception
     */
    public IRStatistics statistics() throws Exception {
        if (dataModel == null) {
            return null;
        }
        //记录开始时间，计算耗时
        long timestamp = System.currentTimeMillis();

        RecommenderIRStatsEvaluator statsEvaluator = new GenericRecommenderIRStatsEvaluator();
        RecommenderBuilder recommenderBuilder = new RecommenderBuilder() {
            @Override
            public Recommender buildRecommender(DataModel model) throws TasteException {
                UserSimilarity similarity = new PearsonCorrelationSimilarity(model);
                UserNeighborhood neighborhood = new NearestNUserNeighborhood(4, similarity, model);
                return new GenericUserBasedRecommender(model, neighborhood, similarity);
            }
        };
        // 计算推荐4个结果时的查准率和召回率
        //使用评估器，并设定评估期的参数
        //4表示"precision and recall at 4"即相当于推荐top4，然后在top-4的推荐上计算准确率和召回率
        IRStatistics stats = statsEvaluator.evaluate(recommenderBuilder, null, dataModel, null, 4, GenericRecommenderIRStatsEvaluator.CHOOSE_THRESHOLD, 1.0);
        logger.error("准确率：" + stats.getPrecision() + "   召回率：" + stats.getRecall() + "  耗时："
                + (System.currentTimeMillis() - timestamp) + " ms");
        return stats;
    }


}
