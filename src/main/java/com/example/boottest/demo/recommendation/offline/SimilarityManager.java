package com.example.boottest.demo.recommendation.offline;

import com.example.boottest.demo.recommendation.offline.math.CollectionUtil;
import com.example.boottest.demo.recommendation.offline.math.PearsonCorrelationUtil;
import com.example.boottest.demo.recommendation.offline.model.Item;
import com.example.boottest.demo.recommendation.offline.model.Rating;
import com.example.boottest.demo.recommendation.offline.model.User;
import com.example.boottest.demo.recommendation.offline.model.UserSimilarity;

import java.util.*;

/**
 * @author Guan
 * @date Created on 2019/3/6
 */
public class SimilarityManager {

    /**
     * 存储用户相似度的数据结构
     * 每个user拥有一个TreeMap，TreeMap里面记录了该用户和其他用户的相似度
     */
    private static Map<User, TreeSet<UserSimilarity>> map = new HashMap<>();


    /**
     * 计算所有用户间的相似度
     *
     * @param users
     */
    public static void refreshSimilarity(User targetUser, Set<User> users) {

        if (null == targetUser) {
            System.out.println("targetUser==null，没有此用户");
            return;
        }

        for (User user1 : users) {
            if (!targetUser.equals(user1)) {
                UserSimilarity similarity = getPearsonCorrelationScore(targetUser, user1);
                putSimilarity(targetUser, similarity);
            }
        }

    }

    /**
     * 计算两个用户的pearson相关系数
     *
     * @param u1
     * @param u2
     * @return
     */
    public static UserSimilarity getPearsonCorrelationScore(User u1, User u2) {

        //同一个用户的相似度是1
        if (Objects.equals(u1, u2)) {
            return new UserSimilarity(u1, u2, 1);
        }

        UserSimilarity similarity = new UserSimilarity(u1, u2, 0);

        List<Double> u1List = new ArrayList<>();
        List<Double> u2List = new ArrayList<>();
        Map<Item, Rating> u1Rating = UserItemMatrixManager.getRatingData(u1);
        Map<Item, Rating> u2Rating = UserItemMatrixManager.getRatingData(u2);

        //有任何一个人的数据是空，即没有评分记录，那么两个人的相似度就为0
        if (u1Rating.isEmpty() || u2Rating.isEmpty()) {
            return similarity;
        }

        //选出u1和u2的共同评分项
        for (Item item : u1Rating.keySet()) {
            if (u2Rating.containsKey(item)) {
                u1List.add(u1Rating.get(item).getRating());
                u2List.add(u2Rating.get(item).getRating());
            }
        }

        //如果两个人的共同评分项为0，那么两个人的相似度也就是0
        if (u1List.size() == 0 || u2List.size() == 0) {
            return similarity;
        }

        //计算两个用户的相似度
        //经典的
        double sim = PearsonCorrelationUtil.getPearsonCorrelationScore(u1List, u2List);
        //共同评分项目因子？
        double factor = (u1List.size() + 0.0) / CollectionUtil.setCount(u1Rating.keySet(), u2Rating.keySet());
        sim = sim * factor;
        similarity.setSimilarity(sim);

        return similarity;

    }


    /**
     * 获取目标用户的最近邻
     *
     * @param user
     * @param n
     * @return
     */
    public static List<UserSimilarity> getNeighborList(User user, int n) {
        List<UserSimilarity> list = new ArrayList<>(n);
        if (!map.containsKey(user)) {
            return list;
        }
        TreeSet<UserSimilarity> neighborSet = map.get(user);
        int i = 0;
        Iterator<UserSimilarity> iterator = neighborSet.iterator();
        while (iterator.hasNext() && i < n) {
            list.add(iterator.next());
            i++;
        }
        return list;
    }

    /**
     * 将 用户相似度存入 map中
     *
     * @param user
     * @param similarity
     */
    private static void putSimilarity(User user, UserSimilarity similarity) {
        if (map.containsKey(user)) {
            map.get(user).add(similarity);
        } else {
            TreeSet<UserSimilarity> set = new TreeSet<>();
            set.add(similarity);
            map.put(user, set);
        }
    }


    /**
     * 计算共同评分因子
     *
     * @param u1Rating
     * @param u2Rating
     * @return
     */
    @Deprecated
    private static double getCommonRatingFactor(Map<Item, Rating> u1Rating, Map<Item, Rating> u2Rating) {

        double intersection = CollectionUtil.intersectionCount(u1Rating.keySet(), u2Rating.keySet());

        double set = CollectionUtil.setCount(u1Rating.keySet(), u2Rating.keySet());

        return intersection / set;

    }


}
