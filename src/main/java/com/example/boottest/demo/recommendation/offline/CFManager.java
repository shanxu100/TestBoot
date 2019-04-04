package com.example.boottest.demo.recommendation.offline;

import com.example.boottest.demo.recommendation.offline.evaluation.EvaluationManager;
import com.example.boottest.demo.recommendation.offline.model.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * @author Guan
 * @date Created on 2019/3/6
 */
public class CFManager {

    private static File data = new File("C:\\Users\\Guan\\dataset\\brightkite\\Brightkite_totalCheckins_rating.txt");


//    private static File data = new File("C:\\Users\\Guan\\dataset\\movielens\\ml-1m\\tmp\\ratings.dat");
//    private static File trainSetFile = new File("C:\\Users\\Guan\\dataset\\movielens\\ml-1m\\tmp\\trainSet.dat");
//    private static File testSetFile = new File("C:\\Users\\Guan\\dataset\\movielens\\ml-1m\\tmp\\testSet.dat");
//    private static File resultSetFile = new File("C:\\Users\\Guan\\dataset\\movielens\\ml-1m\\tmp\\resultSet.dat");

    public static final String SEPARATOR = "\t";
//    public static final String SEPARATOR2 = "\t";

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

        File trainSetFile = new File("C:\\Users\\Guan\\dataset\\brightkite\\dataset\\trainSet.dat");
        File testSetFile = new File("C:\\Users\\Guan\\dataset\\brightkite\\dataset\\testSet.dat");
        DataSetManager.createTrainAndTestSet(data, trainSetFile, testSetFile, SEPARATOR);
    }

    /**
     * 按照一组设定参数运行，产生推荐项，并且进行评估
     */
    public static Evaluation run(File testSetFile, int neighborNum, int rcmdCount, String tag, boolean classic) {

        long time1 = System.currentTimeMillis();

        System.out.println("运算：最近邻数量" + neighborNum + "  推荐项目数：" + rcmdCount + " 开始时间：" + time1);

        Map<User, List<Rating>> resultMap = new HashMap<>();

        for (User targetUser : UserItemMatrixManager.getAllUser()) {

            //计算目标用户和每个用户之间的相似度,获取最近邻
            SimilarityManager.refreshSimilarity(targetUser, UserItemMatrixManager.getAllUser(), classic);
            List<UserSimilarity> neighborList = SimilarityManager.getNeighborList(targetUser, neighborNum);

//            System.out.println("targetUser: " + targetUser.getUserId() + "   最近邻数量：" + neighborList.size());

            //预测评分
            List<Rating> rcmdList = PredictRatingManager
                    .predictRating(targetUser, neighborList, rcmdCount);

//            System.out.println("目标用户：" + targetUser.getUserId() + " 产生推荐项：" + rcmdList.size());
            resultMap.put(targetUser, rcmdList);

        }
        File resultSetFile = new File("C:\\Users\\Guan\\dataset\\movielens\\ml-1m\\" + tag + "\\result\\resultSet_"
                + neighborNum + "_" + rcmdCount + ".dat");

        FileManager.outputFile(resultSetFile, resultMap, SEPARATOR);
        Evaluation evaluation = EvaluationManager.calculatePrecisionRecall(resultSetFile, testSetFile, SEPARATOR);
        evaluation.setCount(neighborNum, rcmdCount);
//        System.out.println(evaluation);
        System.out.println("运行时间：" + (System.currentTimeMillis() - time1));
        return evaluation;


    }


    /**
     * 按照一组设定参数运行，产生推荐项，并且进行评估
     */
    public static double runWithRMSE(File testSetFile, int neighborNum, String tag, boolean classic) {

        long time1 = System.currentTimeMillis();

        System.out.println("运算：最近邻数量" + neighborNum + " 开始时间：" + time1);

        List<String> testList = FileManager.inputFile(testSetFile);
        Map<User, Set<Rating>> test = new HashMap<>();
        for (String line : testList) {
            String[] ss = line.split(SEPARATOR);
            User user = new User(ss[0]);
            Item item = new Item(ss[1]);
            Rating rating = new Rating(user, item, Double.parseDouble(ss[2]));
            if (test.containsKey(user)) {
                test.get(user).add(rating);
            } else {
                Set<Rating> set = new HashSet<>();
                set.add(rating);
                test.put(user, set);
            }
        }

        double RMSE = 0;
        int num = 0;
        for (User targetUser : UserItemMatrixManager.getAllUser()) {

            //计算目标用户和每个用户之间的相似度,获取最近邻
            SimilarityManager.refreshSimilarity(targetUser, UserItemMatrixManager.getAllUser(), classic);
            List<UserSimilarity> neighborList = SimilarityManager.getNeighborList(targetUser, neighborNum);


            Set<Rating> realRatingSet = test.get(targetUser);

            //预测评分
            List<Item> list = new ArrayList<>(realRatingSet.size());
            for (Rating rating : realRatingSet) {
                list.add(rating.getItem());
            }
            List<Rating> predictRatingList = PredictRatingManager
                    .predictRating(targetUser, list, neighborList);

            double rmse = EvaluationManager.calRMSE(realRatingSet, predictRatingList);

//            System.out.println("user="+targetUser.getUserId()+" rmse="+rmse);
            RMSE += rmse;
            num++;
//            System.out.println("目标用户：" + targetUser.getUserId() + " 产生推荐项：" + rcmdList.size());


        }
        if (num != 0) {
            RMSE = RMSE / num;

        }
        RMSE = Math.sqrt(RMSE);
//        File resultSetFile = new File("C:\\Users\\Guan\\dataset\\movielens\\ml-1m\\" + tag + "\\result\\resultSet_"
//                + neighborNum + "_RMSE.dat");


//        System.out.println(evaluation);
        System.out.println("RMSE=" + RMSE);
        System.out.println("运行时间：" + (System.currentTimeMillis() - time1));
        System.out.println("-------------------");
        return RMSE;


    }

    /**
     * 多组参数运行
     */
    public static void autoRun(File testSetFile, File evaluationFile, String tag, boolean classic) {
        List<Evaluation> list = new ArrayList<>(60);

        for (int i = 5; i <= 50; i += 5) {

            Evaluation evaluation = CFManager.run(testSetFile, 20, i, tag, classic);
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


//        for (int i = 5; i <= 50; i += 5) {
//
//            for (int j = 5; j <= 20; j += 5) {
//
//                Evaluation evaluation = CFManager.run(testSetFile, j, i, tag);
//                list.add(evaluation);
//                FileManager.outputFile(evaluationFile, new FileManager.OutputListener() {
//                    @Override
//                    public void output(BufferedWriter bufferedWriter) {
//                        try {
//                            bufferedWriter.write(evaluation.toFormattedString(SEPARATOR));
//                            bufferedWriter.newLine();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//                }, true);
//            }
//
//        }

        for (Evaluation evaluation : list) {
            System.out.println(evaluation.toFormattedString(SEPARATOR));
        }
        System.out.println("----------------------------------");


    }

    public static void lab3() {


        File trainSetFile = new File("C:\\Users\\Guan\\dataset\\movielens\\ml-1m\\lab3\\trainSet.dat");
        File testSetFile = new File("C:\\Users\\Guan\\dataset\\movielens\\ml-1m\\lab3\\testSet.dat");
        File evaluationFile = new File("C:\\Users\\Guan\\dataset\\movielens\\ml-1m\\lab3\\evaluation.dat");

        //加载数据集
        //读数据，构造user-item矩阵
        UserItemMatrixManager.input(trainSetFile, SEPARATOR);
        autoRun(testSetFile, evaluationFile, "lab3", false);

        UserItemMatrixManager.clear();
        PredictRatingManager.clear();
    }

    public static void lab4() {


        File trainSetFile = new File("C:\\Users\\Guan\\dataset\\movielens\\ml-1m\\lab4\\trainSet.dat");
        File testSetFile = new File("C:\\Users\\Guan\\dataset\\movielens\\ml-1m\\lab4\\testSet.dat");
        File evaluationFile = new File("C:\\Users\\Guan\\dataset\\movielens\\ml-1m\\lab4\\evaluation.dat");

        //加载数据集
        //读数据，构造user-item矩阵
        UserItemMatrixManager.input(trainSetFile, SEPARATOR);
        //经典的协同过滤推荐算法
        autoRun(testSetFile, evaluationFile, "lab4", true);
        UserItemMatrixManager.clear();
        PredictRatingManager.clear();

    }

    public static void lab5() {


        File trainSetFile = new File("C:\\Users\\Guan\\dataset\\movielens\\ml-1m\\lab5\\trainSet.dat");
        File testSetFile = new File("C:\\Users\\Guan\\dataset\\movielens\\ml-1m\\lab5\\testSet.dat");
        File evaluationFile = new File("C:\\Users\\Guan\\dataset\\movielens\\ml-1m\\lab5\\evaluation.dat");

        //加载数据集
        //读数据，构造user-item矩阵
        UserItemMatrixManager.input(trainSetFile, SEPARATOR);
        autoRun(testSetFile, evaluationFile, "lab5", true);
        UserItemMatrixManager.clear();
        PredictRatingManager.clear();

    }

    public static void lab6() {


        File trainSetFile = new File("C:\\Users\\Guan\\dataset\\movielens\\ml-1m\\lab6\\trainSet.dat");
        File testSetFile = new File("C:\\Users\\Guan\\dataset\\movielens\\ml-1m\\lab6\\testSet.dat");
        File evaluationFile = new File("C:\\Users\\Guan\\dataset\\movielens\\ml-1m\\lab6\\evaluation.dat");

        //加载数据集
        //读数据，构造user-item矩阵
        UserItemMatrixManager.input(trainSetFile, SEPARATOR);
        autoRun(testSetFile, evaluationFile, "lab6", true);
        UserItemMatrixManager.clear();
        PredictRatingManager.clear();

    }

    public static void lab7() {


        File trainSetFile = new File("C:\\Users\\Guan\\dataset\\movielens\\ml-1m\\lab7\\trainSet.dat");
        File testSetFile = new File("C:\\Users\\Guan\\dataset\\movielens\\ml-1m\\lab7\\testSet.dat");
        File evaluationFile = new File("C:\\Users\\Guan\\dataset\\movielens\\ml-1m\\lab7\\evaluation.dat");

        //加载数据集
        //读数据，构造user-item矩阵
        UserItemMatrixManager.input(trainSetFile, SEPARATOR);
        runWithRMSE(testSetFile, 50, "lab7", true);
        UserItemMatrixManager.clear();
        PredictRatingManager.clear();

    }

    public static void lab8() {


        File trainSetFile = new File("C:\\Users\\Guan\\dataset\\brightkite\\dataset\\trainSet.dat");
        File testSetFile = new File("C:\\Users\\Guan\\dataset\\brightkite\\dataset\\testSet.dat");
        File evaluationFile = new File("C:\\Users\\Guan\\dataset\\brightkite\\lab8\\evaluation.dat");

        //加载数据集
        //读数据，构造user-item矩阵
        UserItemMatrixManager.input(trainSetFile, SEPARATOR);
        runWithRMSE(testSetFile, 100, "lab8", false);
        UserItemMatrixManager.clear();
        PredictRatingManager.clear();

    }

}
