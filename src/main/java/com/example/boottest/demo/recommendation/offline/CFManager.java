package com.example.boottest.demo.recommendation.offline;

import com.example.boottest.demo.recommendation.offline.evaluation.EvaluationManager;
import com.example.boottest.demo.recommendation.offline.model.Evaluation;
import com.example.boottest.demo.recommendation.offline.model.Rating;
import com.example.boottest.demo.recommendation.offline.model.User;
import com.example.boottest.demo.recommendation.offline.model.UserSimilarity;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
    private static File evaluationFile = new File("C:\\Users\\Guan\\dataset\\movielens\\ml-1m\\evaluation.dat");

//    private static File data = new File("C:\\Users\\Guan\\dataset\\movielens\\ml-1m\\tmp\\ratings.dat");
//    private static File trainSetFile = new File("C:\\Users\\Guan\\dataset\\movielens\\ml-1m\\tmp\\trainSet.dat");
//    private static File testSetFile = new File("C:\\Users\\Guan\\dataset\\movielens\\ml-1m\\tmp\\testSet.dat");
//    private static File resultSetFile = new File("C:\\Users\\Guan\\dataset\\movielens\\ml-1m\\tmp\\resultSet.dat");

    public static final String SEPARATOR = "::";

    static {
        //加载数据集
        //读数据，构造user-item矩阵
        UserItemMatrixManager.input(trainSetFile, SEPARATOR);

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
     * 按照一组设定参数运行，产生推荐项，并且进行评估
     */
    public static Evaluation run(int neighborNum, int rcmdCount) {

        long time1 = System.currentTimeMillis();

        System.out.println("运算：最近邻数量" + neighborNum + "  推荐项目数：" + rcmdCount + " 开始时间：" + time1);

        Map<User, List<Rating>> resultMap = new HashMap<>();

        for (User targetUser : UserItemMatrixManager.getAllUser()) {

            //计算目标用户和每个用户之间的相似度,获取最近邻
            SimilarityManager.refreshSimilarity(targetUser, UserItemMatrixManager.getAllUser());
            List<UserSimilarity> neighborList = SimilarityManager.getNeighborList(targetUser, neighborNum);

//            System.out.println("targetUser: " + targetUser.getUserId() + "   最近邻数量：" + neighborList.size());

            //预测评分
            List<Rating> rcmdList = PredictRatingManager
                    .predictRating(targetUser, neighborList, rcmdCount);

//            System.out.println("目标用户：" + targetUser.getUserId() + " 产生推荐项：" + rcmdList.size());
            resultMap.put(targetUser, rcmdList);

        }
        File resultSetFile = new File("C:\\Users\\Guan\\dataset\\movielens\\ml-1m\\result\\resultSet_"
                + neighborNum + "_" + rcmdCount + ".dat");

        FileManager.outputFile(resultSetFile, resultMap, SEPARATOR);
        Evaluation evaluation = EvaluationManager.calculatePrecisionRecall(resultSetFile, testSetFile, SEPARATOR);
        evaluation.setCount(neighborNum, rcmdCount);
//        System.out.println(evaluation);
        System.out.println("运行时间：" + (System.currentTimeMillis() - time1));
        return evaluation;


    }


    /**
     * 多组参数运行
     */
    public static void autoRun() {
        List<Evaluation> list = new ArrayList<>(60);

        for (int i = 5; i <= 50; i += 5) {

            for (int j = 5; j <= 20; j += 5) {

                Evaluation evaluation = CFManager.run(j, i);
                list.add(evaluation);
                FileManager.outputFile(evaluationFile, new FileManager.OutputListener() {
                    @Override
                    public void output(BufferedWriter bufferedWriter) {
                        try {
                            bufferedWriter.write(evaluation.toFormattedString(SEPARATOR));
                            bufferedWriter.newLine();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }, true);
            }

        }

        for (Evaluation evaluation : list) {
            System.out.println(evaluation.toFormattedString(SEPARATOR));
        }


    }

}
