package com.example.boottest.demo.recommendation.offline.brightkite.slopeone;

import java.io.File;

/**
 * @author Guan
 * @date Created on 2019/4/12
 */
public class SlopeOneMain {


    public static void main(String[] args) {
        File trainSetFile = new File("C:\\Users\\Guan\\dataset\\brightkite\\lab8\\trainSet.dat");
        File testSetFile = new File("C:\\Users\\Guan\\dataset\\brightkite\\lab8\\testSet.dat");

        SlopeOneManager manager = new SlopeOneManager();
        manager.runWithREMS(trainSetFile, testSetFile);

    }

}
