package com.example.boottest.demo.recommendation.offline.brightkite.slopeone;

import com.example.boottest.demo.recommendation.offline.FileManager;
import com.example.boottest.demo.recommendation.offline.evaluation.EvaluationManager;
import com.example.boottest.demo.recommendation.offline.model.Item;
import com.example.boottest.demo.recommendation.offline.model.Rating;
import com.example.boottest.demo.recommendation.offline.model.User;

import java.io.File;
import java.util.*;

/**
 * @author Guan
 * @date Created on 2019/4/12
 */
public class SlopeOneManager {

    public static final String SEPARATOR = "\t";


    public double runWithREMS(File trainSetFile, File testSetFile) {
        long time1 = System.currentTimeMillis();

        System.out.println("运算： 开始时间：" + time1);
        //加载训练集
        SlopeOneDataSet.input(trainSetFile, SEPARATOR);

        //加载测试集
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

        //计算指标
        double RMSE = 0;
        int num = 0;

        for (User targetUser : test.keySet()) {

            //真实评分的itemList
            Set<Rating> realRatingSet = test.get(targetUser);

            //预测评分的itemList
            List<Item> list = new ArrayList<>(realRatingSet.size());
            for (Rating rating : realRatingSet) {
                list.add(rating.getItem());
            }
            List<Rating> predictRatingList = getPredictRatingList(targetUser, list);

            double rmse = EvaluationManager.calRMSE(realRatingSet, predictRatingList);
            RMSE += rmse;
            num++;
        }


        if (num != 0) {
            RMSE = RMSE / num;
        }
        RMSE = Math.sqrt(RMSE);
        System.out.println("RMSE=" + RMSE);
        System.out.println("运行时间：" + (System.currentTimeMillis() - time1));
        System.out.println("-------------------");
        return RMSE;

    }

    /**
     * @param user
     * @param list
     * @return
     */
    private List<Rating> getPredictRatingList(User user, List<Item> list) {

        List<Rating> ratingList = new ArrayList<>(list.size());
        for (Item item : list) {
            Rating rating = new Rating(user, item, PredictRating(user, item));
            ratingList.add(rating);
        }
        return ratingList;
    }

    /**
     * 预测user对targetItem的预测评分
     *
     * @param user
     * @param targetItem
     * @return
     */
    private double PredictRating(User user, Item targetItem) {

        Set<Item> itemSet = SlopeOneDataSet.getAllItem();
        int count = 0;
        double result = 0;
        for (Item item : itemSet) {
            try {
                double rating = getDiffValue(user, targetItem, item);
                if (rating != -1) {
                    count++;
                    result += rating;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        if (count == 0) {
            return -1;
        }
        return result / count;

    }


    /**
     * 计算item对targetItem的预测评分
     *
     * @param targetItem
     * @param item
     * @return
     */
    private double getDiffValue(User targetUser, Item targetItem, Item item) {

        Map<User, Rating> map1 = SlopeOneDataSet.get(targetItem);
        if (map1 == null) {
            //没有人对targetItem评过分，冷启动问题，直接返回一个默认值。
            return 3.0;
        }
        if (map1.containsKey(targetUser)) {
            //目标用户已评分，不用预测，直接返回评分
            return map1.get(targetUser).getRating();
        }

        Map<User, Rating> map2 = SlopeOneDataSet.get(item);

        try {
            if (map2 == null || !map2.containsKey(targetUser)) {
                //targetUser没有评价过item，因此无法通过item预测targetItem的评分
                return -1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        int count = 0;
        double result = 0;
        for (User user : map1.keySet()) {
            if (map2.containsKey(user)) {
                double newRating = (map2.get(user).getRating() - map1.get(user).getRating());
//                newRating = newRating > 5 ? 5 : newRating;
//                result += newRating > 5 ? 5 : newRating;
                result += newRating;
                count++;
            }
        }
        if (count == 0) {
            return -1;
        }
        result = result / count + map2.get(targetUser).getRating();
        result = result > 5 ? 5 : result;
        result = result < 0 ? 0 : result;
        return result;

    }

}
