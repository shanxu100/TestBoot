package com.example.boottest.demo.recommendation.offline;

import com.example.boottest.demo.recommendation.offline.model.Item;
import com.example.boottest.demo.recommendation.offline.model.User;
import com.example.boottest.demo.recommendation.offline.model.UserSimilarity;

import java.util.List;

/**
 * @author Guan
 * @date Created on 2019/3/6
 */
public class PredictRatingManager {

    public static void predictRating(User user, List<UserSimilarity> neighborList) {

        System.out.println("targetUser: " + user.getUserId());
        System.out.println("最近邻数量：" + neighborList.size());
        for (UserSimilarity similarity : neighborList) {
            System.out.println(similarity.getU2().getUserId() + "----" + similarity.getSimilarity());
        }

    }

    public static void predictRating(User user, Item item) {

    }

}
