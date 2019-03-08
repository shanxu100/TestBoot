package com.example.boottest.demo.recommendation.offline;

import com.example.boottest.demo.recommendation.offline.model.Rating;
import com.example.boottest.demo.recommendation.offline.model.User;
import com.example.boottest.demo.recommendation.offline.model.UserSimilarity;

import java.io.File;
import java.util.List;

/**
 * @author Guan
 * @date Created on 2019/3/6
 */
public class CFManager {

    static {
    }

    private CFManager() {

    }

    private static CFManager getInstance() {
        return null;
    }

    public static void run() {
//        File data = new File("C:\\Users\\Guan\\dataset\\locationRcmd\\train.txt");
        File data = new File("C:\\Users\\Guan\\dataset\\movielens\\ml-1m\\ratings.dat");

        String targetUserId = "1";

        //读数据，构造user-item矩阵
        UserItemMatrixManager.input(data, "::");

        //计算目标用户和每个用户之间的相似度,获取最近邻
        User targetUser = UserItemMatrixManager.getUser(targetUserId);
        SimilarityManager.refreshSimilarity(targetUser, UserItemMatrixManager.getAllUser());
        List<UserSimilarity> neighborList = SimilarityManager.getNeighborList(targetUser, 10);

        //预测评分
        List<Rating> rcmdList = PredictRatingManager
                .predictRating(targetUser, neighborList, 5);
        for (Rating rating : rcmdList) {
            System.out.println(rating.getUser().getUserId()
                    + " -- " + rating.getItem().getItemId()
                    + " -- " + rating.getRating());
        }

    }

}
