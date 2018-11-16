package com.example.boottest.demo.recommendation;


import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.apache.mahout.cf.taste.similarity.precompute.example.GroupLensDataModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;

/**
 * @author ahu_lichang
 * @date 2017/6/23
 */
@Component
@Deprecated
public class UBRCMD {


    @Autowired
    @Qualifier("datasetPath")
    String datasetPath;


    public List<RecommendedItem> recommend(long userId, int howMany) throws Exception {
        long time1 = System.currentTimeMillis();
        //准备数据 这里是电影评分数据
        File file = new File(datasetPath);
        //将数据加载到内存中，GroupLensDataModel是针对开放电影评论数据的
        DataModel dataModel = new GroupLensDataModel(file);

        System.out.println("加载DataModel时间：" + (System.currentTimeMillis() - time1));
        time1 = System.currentTimeMillis();

        //计算相似度，相似度算法有很多种，欧几里得、皮尔逊等等。
        UserSimilarity similarity = new PearsonCorrelationSimilarity(dataModel);
        //计算最近邻域，邻居有两种算法，基于固定数量的邻居和基于相似度的邻居，这里使用基于固定数量的邻居
        UserNeighborhood userNeighborhood = new NearestNUserNeighborhood(100, similarity, dataModel);
        //构建推荐器，协同过滤推荐有两种，分别是基于用户的和基于物品的，这里使用基于用户的协同过滤推荐
        Recommender recommender = new GenericUserBasedRecommender(dataModel, userNeighborhood, similarity);

        System.out.println("构建其他项时间：" + (System.currentTimeMillis() - time1));
        time1 = System.currentTimeMillis();

        //给用户ID等于5的用户推荐10部电影
        List<RecommendedItem> recommendedItemList = recommender.recommend(userId, howMany);
        System.out.println("产生推荐：" + (System.currentTimeMillis() - time1));
        time1 = System.currentTimeMillis();
        recommendedItemList = recommender.recommend(2, howMany);
        System.out.println("产生推荐：" + (System.currentTimeMillis() - time1));
        time1 = System.currentTimeMillis();
        //打印推荐的结果
        System.out.println("使用基于用户的协同过滤算法");
        System.out.println("为用户:" + userId + "推荐 " + howMany + " 个商品");
        for (RecommendedItem recommendedItem : recommendedItemList) {
            System.out.println(recommendedItem);
        }
        return recommendedItemList;

    }

    public static void main(String[] args) throws Exception {


    }
}