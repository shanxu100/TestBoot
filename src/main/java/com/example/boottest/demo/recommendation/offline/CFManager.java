package com.example.boottest.demo.recommendation.offline;

import com.example.boottest.demo.recommendation.offline.evaluation.EvaluationManager;
import com.example.boottest.demo.recommendation.offline.model.Evaluation;
import com.example.boottest.demo.recommendation.offline.model.Rating;
import com.example.boottest.demo.recommendation.offline.model.User;
import com.example.boottest.demo.recommendation.offline.model.UserSimilarity;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Guan
 * @date Created on 2019/3/6
 */
public class CFManager {

    private static File data = new File("C:\\Users\\Guan\\dataset\\movielens\\ml-1m\\ratings.dat");
    private static File trainSetFile = new File("C:\\Users\\Guan\\dataset\\movielens\\ml-1m\\trainSet.dat");
    private static File testSetFile = new File("C:\\Users\\Guan\\dataset\\movielens\\ml-1m\\testSet.dat");
    private static File resultSetFile = new File("C:\\Users\\Guan\\dataset\\movielens\\ml-1m\\resultSet.dat");

//    private static File data = new File("C:\\Users\\Guan\\dataset\\movielens\\ml-1m\\tmp\\ratings.dat");
//    private static File trainSetFile = new File("C:\\Users\\Guan\\dataset\\movielens\\ml-1m\\tmp\\trainSet.dat");
//    private static File testSetFile = new File("C:\\Users\\Guan\\dataset\\movielens\\ml-1m\\tmp\\testSet.dat");
//    private static File resultSetFile = new File("C:\\Users\\Guan\\dataset\\movielens\\ml-1m\\tmp\\resultSet.dat");

    public static final String SEPARATOR = "::";

    static {
    }

    private CFManager() {

    }

    private static CFManager getInstance() {
        return null;
    }

    /**
     * 拆分原始数据集，构造测试集和训练集
     */
    public static void prefixRun() {

        DataSetManager.createTrainAndTestSet(data, trainSetFile, testSetFile, SEPARATOR);
    }

    /**
     * 开始运行，产生推荐项
     */
    public static void run() {

        //读数据，构造user-item矩阵
        UserItemMatrixManager.input(trainSetFile, SEPARATOR);


        Map<User, List<Rating>> resultMap = new HashMap<>();

        for (User targetUser : UserItemMatrixManager.getAllUser()) {

            //计算目标用户和每个用户之间的相似度,获取最近邻
            SimilarityManager.refreshSimilarity(targetUser, UserItemMatrixManager.getAllUser());
            List<UserSimilarity> neighborList = SimilarityManager.getNeighborList(targetUser, 10);

            System.out.println("targetUser: " + targetUser.getUserId() + "   最近邻数量：" + neighborList.size());
//            for (UserSimilarity similarity : neighborList) {
//                System.out.println(similarity.toString());
//            }


            //预测评分
            List<Rating> rcmdList = PredictRatingManager
                    .predictRating(targetUser, neighborList, 50);
//            for (Rating rating : rcmdList) {
//                System.out.println(rating.getUser().getUserId()
//                        + " -- " + rating.getItem().getItemId()
//                        + " -- " + rating.getRating());
//            }
            System.out.println("目标用户：" + targetUser.getUserId() + " 产生推荐项：" + rcmdList.size());
            resultMap.put(targetUser, rcmdList);

        }
        FileManager.outputFile(resultSetFile, resultMap, SEPARATOR);


    }

    /**
     * 评价
     */
    public static void postfixRun() {
        Evaluation evaluation = EvaluationManager.calculatePrecisionRecall(resultSetFile, testSetFile, SEPARATOR);

        System.out.println(evaluation);

    }

}
