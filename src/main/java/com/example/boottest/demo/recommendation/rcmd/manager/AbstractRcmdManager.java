package com.example.boottest.demo.recommendation.rcmd.manager;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * @author Guan
 * @date Created on 2018/12/12
 */
@Component
public abstract class AbstractRcmdManager {

    private static final Logger logger = LoggerFactory.getLogger(AbstractRcmdManager.class);


    protected DataModel dataModel;

    @Autowired
    @Qualifier("datasetPath")
    protected String datasetPath;

    @Autowired
    @Qualifier("NN")
    private int NN;


    /**
     * 正在评估模型
     */
    private static boolean isEvaluating = false;

    /**
     * 正在计算
     */
    private static boolean isStatistics = false;

    /**
     * 初始化
     */
    public void init() {
        dataModel = loadDataModel();
    }

    /**
     * 加载DataModel
     */
    public abstract DataModel loadDataModel();


    /**
     * 产生推荐列表
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
        UserNeighborhood userNeighborhood = new NearestNUserNeighborhood(4, 0, similarity, dataModel, 1);
//        UserNeighborhood userNeighborhood = new ThresholdUserNeighborhood(0.1, similarity, dataModel,0.5);
        //构建推荐器，协同过滤推荐有两种，分别是基于用户的和基于物品的，这里使用基于用户的协同过滤推荐
        Recommender recommender = new GenericUserBasedRecommender(dataModel, userNeighborhood, similarity);

        //给用户ID等于5的用户推荐10部电影
        List<RecommendedItem> recommendedItemList = recommender.recommend(userId, howMany, true);

        //打印推荐的结果
        logger.info("使用基于用户的协同过滤算法,为用户: {} 推荐 {} 个商品。耗时：{} ms。",
                userId, howMany, System.currentTimeMillis() - startTime);
        for (RecommendedItem recommendedItem : recommendedItemList) {
            logger.debug(recommendedItem.toString());
        }
        return recommendedItemList;
    }

    /**
     * 评估模型
     *
     * @return
     * @throws Exception
     */
    public double evaluate() {
        if (dataModel == null) {
            logger.error("dataModel == null，数据未初始化...");
            return -1;
        }

        if (isEvaluating) {
            logger.info("正在评估模型...请勿重复调用");
            return -1;
        }


        try {
            isEvaluating = true;
            long startTime = System.currentTimeMillis();

            //推荐评估，使用均方根
            //RecommenderEvaluator evaluator = new RMSRecommenderEvaluator();
            //推荐评估，使用平均差值
            RecommenderEvaluator evaluator = new AverageAbsoluteDifferenceRecommenderEvaluator();
            RecommenderBuilder builder = new RecommenderBuilder() {
                @Override
                public Recommender buildRecommender(DataModel dataModel) throws TasteException {
                    UserSimilarity similarity = new PearsonCorrelationSimilarity(dataModel);
                    UserNeighborhood neighborhood = new NearestNUserNeighborhood(NN, similarity, dataModel);
                    return new GenericUserBasedRecommender(dataModel, neighborhood, similarity);
                }
            };
            // 用70%的数据用作训练，剩下的30%用来测试.最后得出的评估值越小，说明推荐结果越好
            double score = evaluator.evaluate(builder, null, dataModel, 0.7, 1.0);

            LogUtils.printRunningTime("模型评估，score=" + score, startTime);
            return score;

        } catch (Exception e) {
            e.printStackTrace();
            logger.error(logger.getName(), e);
            return -1;
        } finally {
            isEvaluating = false;
        }


    }

    /**
     * 计算准确率召回率
     * TODO 未完成
     *
     * @throws Exception
     */
    public IRStatistics statistics() {
        if (dataModel == null) {
            logger.error("dataModel == null，数据未初始化...");
            return null;
        }
        if (isStatistics) {
            logger.info("正在计算准确率、召回率和F-Measure....请勿重复调用");
            return null;
        }
        try {
            isStatistics = true;

            //记录开始时间，计算耗时
            long timestamp = System.currentTimeMillis();

            RecommenderIRStatsEvaluator statsEvaluator = new GenericRecommenderIRStatsEvaluator();
            RecommenderBuilder recommenderBuilder = new RecommenderBuilder() {
                @Override
                public Recommender buildRecommender(DataModel model) throws TasteException {
                    UserSimilarity similarity = new PearsonCorrelationSimilarity(model);
                    UserNeighborhood neighborhood = new NearestNUserNeighborhood(NN, similarity, model);
                    return new GenericUserBasedRecommender(model, neighborhood, similarity);
                }
            };
            // 计算推荐4个结果时的查准率和召回率
            //使用评估器，并设定评估期的参数
            //4表示"precision and recall at 4"即相当于推荐top4，然后在top-4的推荐上计算准确率和召回率
            IRStatistics stats = statsEvaluator.evaluate(recommenderBuilder, null, dataModel,
                    null, 4, GenericRecommenderIRStatsEvaluator.CHOOSE_THRESHOLD, 1.0);
            logger.error("准确率：" + stats.getPrecision() + "   召回率：" + stats.getRecall() + "  耗时："
                    + (System.currentTimeMillis() - timestamp) + " ms");
            isStatistics = false;
            return stats;

        } catch (Exception e) {
            e.printStackTrace();
            logger.error(logger.getName(), e);
            return null;
        } finally {
            isStatistics = false;
        }


    }

}
