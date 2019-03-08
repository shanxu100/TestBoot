package com.example.boottest.demo.recommendation.offline;

import com.example.boottest.demo.recommendation.offline.model.Item;
import com.example.boottest.demo.recommendation.offline.model.Rating;
import com.example.boottest.demo.recommendation.offline.model.User;
import com.example.boottest.demo.recommendation.offline.model.UserSimilarity;

import java.util.*;

/**
 * @author Guan
 * @date Created on 2019/3/6
 */
public class PredictRatingManager {

    /**
     * 存储用户预测评分的数据结构
     * 每个user拥有一个TreeMap，TreeMap里面记录了该用户对Item的预测评分
     */
    private static Map<User, TreeSet<Rating>> map = new HashMap<>();


    public static List<Rating> predictRating(User user, List<UserSimilarity> neighborList, int k) {

        if (null == user) {
            System.out.println("没有目标用户，无法预测评分...");
            return new ArrayList<Rating>(0);
        }

        System.out.println("targetUser: " + user.getUserId() + "   最近邻数量：" + neighborList.size());
//        for (UserSimilarity similarity : neighborList) {
//            System.out.println(similarity.getU2().getUserId() + "----" + similarity.getSimilarity());
//        }

        //user的未评分列表
        Set<Item> ratingItemSet = findRatingItemSet(user, neighborList);

        //计算user所有未评分项的预测评分，加入到map中存储
        for (Item item : ratingItemSet) {
            Rating rating = predictRating(user, item, neighborList);
            add(user, rating);
        }

        return getMaxRatingItems(user, k);


    }

    /**
     * 查找目标用户User未评分，而最近邻Users评过分的Item集合
     *
     * @param user
     * @param neighborList
     */
    private static Set<Item> findRatingItemSet(User user, List<UserSimilarity> neighborList) {
        //User的未评分列表
        Set<Item> ratingItemSet = new HashSet<>();
        //user的已评分列表
        Set<Item> ratedItemSet = UserItemMatrixManager.getRatingData(user).keySet();
        for (UserSimilarity userSimilarity : neighborList) {
            //最近邻的已评分列表
            Set<Item> tmpSet = UserItemMatrixManager.getRatingData(userSimilarity.getU2()).keySet();
            for (Item item : tmpSet) {
                if (!ratedItemSet.contains(tmpSet)) {
                    ratingItemSet.add(item);
                }
            }
        }
        return ratingItemSet;
    }


    /**
     * 保存用户User的对Item的预测评分
     *
     * @param user
     * @param rating
     */
    private static void add(User user, Rating rating) {
        if (map.containsKey(user)) {
            map.get(user).add(rating);
            return;
        }
        TreeSet<Rating> treeSet = new TreeSet<>();
        treeSet.add(rating);
        map.put(user, treeSet);

    }

    /**
     * 获取用户User预测评分最高的k个Item
     *
     * @param user
     */
    private static List<Rating> getMaxRatingItems(User user, int k) {

        List<Rating> list = new ArrayList<>(k);
        Iterator<Rating> iterator = map.get(user).iterator();
        int i = 0;
        while (iterator.hasNext() && i < k) {
            list.add(iterator.next());
            i++;
        }

        return list;
    }


    /**
     * 计算 user 对 Item 的预测评分
     *
     * @param user
     * @param item
     * @param neighborList
     * @return
     */
    private static Rating predictRating(User user, Item item, List<UserSimilarity> neighborList) {

        Rating rating;
        if ((rating = UserItemMatrixManager.getRatingData(user, item)) != null) {
            //说明用户已经对该项目评过分，无需计算，直接返回该对象
            return rating;
        }

        double ratingAvg = findRatingAvg(user);

        //分子
        double numerator = 0;
        //分母
        double denominator = 0;


        for (UserSimilarity similarity : neighborList) {

            User neighborUser = similarity.getU2();
            Rating tmpRating;
            if ((tmpRating = UserItemMatrixManager.getRatingData(neighborUser, item)) == null) {
                //最近邻中的用户对item没有评分
                continue;
            }

            numerator += (similarity.getSimilarity() *
                    (tmpRating.getRating() - findRatingAvg(neighborUser)));

            denominator += similarity.getSimilarity();

        }
        double result = denominator == 0 ? 0 : (ratingAvg + numerator / denominator);

        rating = new Rating(user, item, result);
        return rating;
    }

    /**
     * 查找用户的对所有项目评分的均值
     *
     * @param user
     * @return
     */
    private static double findRatingAvg(User user) {
        Map<Item, Rating> map = UserItemMatrixManager.getRatingData(user);
        if (map.size() == 0) {
            return 0;
        }
        double avg = 0;
        for (Map.Entry<Item, Rating> entry : map.entrySet()) {
            avg += entry.getValue().getRating();
        }
        return avg / map.size();
    }

}
