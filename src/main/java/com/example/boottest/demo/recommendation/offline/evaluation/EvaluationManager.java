package com.example.boottest.demo.recommendation.offline.evaluation;

import com.example.boottest.demo.recommendation.offline.FileManager;
import com.example.boottest.demo.recommendation.offline.model.Evaluation;
import com.example.boottest.demo.recommendation.offline.model.Item;
import com.example.boottest.demo.recommendation.offline.model.User;

import java.io.File;
import java.util.*;

/**
 * @author Guan
 * @date Created on 2019/3/8
 */
public class EvaluationManager {


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


    public static Evaluation calculatePrecisionRecall(File resultSetFile, File testSetFile, String separator) {

        List<String> trainList = FileManager.inputFile(resultSetFile);
        List<String> testList = FileManager.inputFile(testSetFile);
        Map<User, Set<Item>> result = new HashMap<>();
        Map<User, Set<Item>> test = new HashMap<>();
        for (String line : trainList) {
            String[] ss = line.split(separator);
            User user = new User(ss[0]);
            Item item = new Item(ss[1]);

            put(user, item, result);
        }
        for (String line : testList) {
            String[] ss = line.split(separator);
            User user = new User(ss[0]);
            Item item = new Item(ss[1]);
            put(user, item, test);
        }

        return calculatePrecisionRecall(result, test);


    }


    public static Evaluation calculatePrecisionRecall2(Map<User, List<Item>> result, Map<User, List<Item>> test) {
        Map<User, Set<Item>> result2 = new HashMap<>();
        Map<User, Set<Item>> test2 = new HashMap<>();

        for (User user : result.keySet()) {
            result2.put(user, new HashSet<>(result.get(user)));
        }

        for (User user : test.keySet()) {
            test2.put(user, new HashSet<>(test.get(user)));
        }

        return calculatePrecisionRecall(result2, test2);

    }

    public static Evaluation calculatePrecisionRecall(Map<User, Set<Item>> result, Map<User, Set<Item>> test) {

        System.out.println("开始评价");
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
                precision += (numerator > denominatorPrecision ? 1 : numerator / denominatorPrecision);
            } else {
                //说明该用户的推荐结果列表为空
            }
            if (denominatorRecall != 0) {
                recall += (numerator > denominatorRecall ? 1 : numerator / denominatorRecall);
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


    private static void put(User user, Item item, Map<User, Set<Item>> map) {

        if (map.containsKey(user)) {
            map.get(user).add(item);
            return;
        }
        Set<Item> set = new HashSet<>();
        set.add(item);
        map.put(user, set);

    }


}
