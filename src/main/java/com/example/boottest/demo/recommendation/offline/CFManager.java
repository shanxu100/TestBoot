package com.example.boottest.demo.recommendation.offline;

import com.example.boottest.demo.recommendation.offline.model.User;

import java.io.File;

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

        //计算目标用户和每个用户之间的相似度
        User targetUser = UserItemMatrixManager.getUser(targetUserId);
        SimilarityManager.refreshSimilarity(targetUser, UserItemMatrixManager.getAllUser());

        //预测评分
        PredictRatingManager.predictRating(targetUser, SimilarityManager.getNeighborList(targetUser, 3));

    }

}
