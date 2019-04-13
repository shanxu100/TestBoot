package com.example.boottest.demo.recommendation.offline.brightkite;

import com.example.boottest.demo.recommendation.offline.CFManager;
import com.example.boottest.demo.recommendation.offline.DataSetManager;
import com.example.boottest.demo.recommendation.offline.PredictRatingManager;
import com.example.boottest.demo.recommendation.offline.UserItemMatrixManager;
import com.example.boottest.demo.recommendation.offline.brightkite.lab9.PositionMap;

import java.io.File;

/**
 * @author Guan
 * @date Created on 2019/3/19
 */
public class BrightKiteMain {
    private static final String SEPARATOR = "\t";

    public static void main(String[] args) {
        File file = new File("C:\\Users\\Guan\\dataset\\brightkite\\lab8\\20POI_UserItemRating.txt");
        File trainSetFile = new File("C:\\Users\\Guan\\dataset\\brightkite\\lab8\\trainSet.dat");
        File testSetFile = new File("C:\\Users\\Guan\\dataset\\brightkite\\lab8\\testSet.dat");
//        DataSetManager.createTrainAndTestSet(file, trainSetFile, testSetFile, SEPARATOR);

        //加载数据集
        //读数据，构造user-item矩阵
        UserItemMatrixManager.input(trainSetFile, SEPARATOR);
        CFManager.runWithRMSE(testSetFile, 30, "lab8", false);
        UserItemMatrixManager.clear();
        PredictRatingManager.clear();


//        PositionMap.createPositionFile(new File("C:\\Users\\Guan\\dataset\\brightkite\\lab9\\Brightkite_totalCheckins_20POI.txt"));

//        PositionMap.processRegeo();
    }

}
