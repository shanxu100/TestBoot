package com.example.boottest.demo.recommendation.offline.evaluation;

import com.example.boottest.demo.recommendation.offline.model.Evaluation;
import com.example.boottest.demo.recommendation.offline.model.Item;
import com.example.boottest.demo.recommendation.offline.model.User;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Guan
 * @date Created on 2019/3/8
 */
public class evaluationManager {


    public static void main(String[] args) {
        User u1 = new User("u1");
        Set<Item> resultSet = new HashSet<>();
        for (int i = 0; i < 10; i++) {
            resultSet.add(new Item(i + ""));
        }

        Set<Item> testSet = new HashSet<>();
        testSet.add(new Item("1"));
        testSet.add(new Item("2"));
        testSet.add(new Item("3"));
        testSet.add(new Item("11"));
        testSet.add(new Item("12"));
        testSet.add(new Item("13"));

        Map<User, Set<Item>> result = new HashMap<>();
        result.put(u1, resultSet);

        Map<User, Set<Item>> test = new HashMap<>();
        test.put(u1, testSet);

        System.out.println(calculatePrecisionRecall(result, test));

    }


    public static Evaluation calculatePrecisionRecall(Map<User, Set<Item>> result, Map<User, Set<Item>> test) {

        //没有用户
        if (result.size() == 0 || test.size() == 0) {
            return new Evaluation(0, 0, 0);
        }

        Evaluation evaluation;


        //所有用户的准确率
        double precision = 0;
        //所有用户的召回率
        double recall = 0;
        double f_measure = 0;
        //记录人数，用于最后计算多个人的召回率的平均值
        int num = 0;

        for (User user : result.keySet()) {
            //计算单个用户的准确率、召回率


            //分子
            int numerator = intersection(result.get(user), test.get(user));
            //分母
            double denominatorPrecision = result.get(user).size();
            double denominatorRecall = test.get(user).size();

            if (denominatorPrecision != 0) {
                precision += (numerator / denominatorPrecision);
            } else {
                //说明该用户的推荐结果列表为空
            }
            if (denominatorRecall != 0) {
                recall += (numerator / denominatorRecall);
            } else {
                //说明该用户的测试集列表为空
            }

            num++;
        }
        precision = precision / num;
        recall = recall / num;
        f_measure = 2 / ((1 / precision) + (1 / recall));
        evaluation = new Evaluation(precision, recall, f_measure);
        return evaluation;

    }


    /**
     * 寻找两个集合的交集
     *
     * @param s1
     * @param s2
     * @return
     */
    private static int intersection(Set s1, Set s2) {

        if (s1.size() == 0 || s2.size() == 0) {
            return 0;
        }

        int count = 0;
        for (Object object : s1) {
            count = s2.contains(object) ? count + 1 : count;
        }
        return count;
    }


}
