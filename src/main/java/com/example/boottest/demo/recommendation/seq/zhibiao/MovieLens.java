package com.example.boottest.demo.recommendation.seq.zhibiao;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.eval.IRStatistics;
import org.apache.mahout.cf.taste.eval.RecommenderBuilder;
import org.apache.mahout.cf.taste.eval.RecommenderIRStatsEvaluator;
import org.apache.mahout.cf.taste.impl.eval.GenericRecommenderIRStatsEvaluator;
import org.apache.mahout.cf.taste.impl.eval.LoadEvaluator;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.apache.mahout.cf.taste.similarity.precompute.example.GroupLensDataModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Guan
 * @date Created on 2019/1/8
 */
public class MovieLens {
    public static void main(String[] args) throws Exception {
        //使用定制的GrouplensDataModel,如果没有转换数据集成为csv格式的
        DataModel dataModel = new GroupLensDataModel(new File(
                "C:\\Users\\Guan\\dataset\\movielens\\ml-1m\\ratings.dat"));
        //皮尔逊相关系数，衡量用户相似度
        UserSimilarity userSimilarity = new PearsonCorrelationSimilarity(
                dataModel);
//        //构建用户邻居，100个
//        UserNeighborhood userNeighborhood = new NearestNUserNeighborhood(100,
//                userSimilarity, dataModel);
//        //推荐引擎
//        Recommender recommender = new GenericUserBasedRecommender(dataModel,
//                userNeighborhood, userSimilarity);
        //运行
//        LoadEvaluator.runLoad(recommender);
//        stats(dataModel, 5);
//        stats(dataModel, 10);
//        stats(dataModel, 15);
//        stats(dataModel, 20);
//        stats(dataModel, 25);
        List<IRStatistics> list = new ArrayList();
        for (int i = 0; i < 10; i++) {
            IRStatistics statistics = stats(dataModel, 5 + i * 5);
            list.add(statistics);
        }
        for (int i = 0; i < 10; i++) {
            System.err.println("at:" + (5 + i * 5));
            IRStatistics stats = list.get(i);
            System.out.println("准确率：" + stats.getPrecision() + "   召回率：" + stats.getRecall()
                    + "    Fmeasure:" + stats.getF1Measure());
        }

    }

    private static IRStatistics stats(DataModel dataModel, int at) throws Exception {
        //记录开始时间，计算耗时
        long timestamp = System.currentTimeMillis();

        RecommenderIRStatsEvaluator statsEvaluator = new GenericRecommenderIRStatsEvaluator();
        RecommenderBuilder recommenderBuilder = new RecommenderBuilder() {
            @Override
            public Recommender buildRecommender(DataModel model) throws TasteException {
                UserSimilarity similarity = new PearsonCorrelationSimilarity(model);
                UserNeighborhood neighborhood = new NearestNUserNeighborhood(10, similarity, model);
                return new GenericUserBasedRecommender(model, neighborhood, similarity);
            }
        };
        // 计算推荐4个结果时的查准率和召回率
        //使用评估器，并设定评估期的参数
        //4表示"precision and recall at 4"即相当于推荐top4，然后在top-4的推荐上计算准确率和召回率
        IRStatistics stats = statsEvaluator.evaluate(recommenderBuilder, null, dataModel,
                null, at, GenericRecommenderIRStatsEvaluator.CHOOSE_THRESHOLD, 1.0);
        System.out.println("准确率：" + stats.getPrecision() + "   召回率：" + stats.getRecall()
                + "    Fmeasure:" + stats.getF1Measure() + "  耗时："
                + (System.currentTimeMillis() - timestamp) + " ms");
        return stats;
    }

}
