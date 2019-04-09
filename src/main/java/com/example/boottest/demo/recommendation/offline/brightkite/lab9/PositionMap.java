package com.example.boottest.demo.recommendation.offline.brightkite.lab9;

import com.example.boottest.demo.recommendation.geo.GeoUtil;
import com.example.boottest.demo.recommendation.offline.FileManager;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Guan
 * @date Created on 2019/4/9
 */
public class PositionMap {


    /**
     * 提取原数据集中的地点，输出到地点数据文件
     *
     * @param file
     */
    public static void createPositionFile(File file) {


    }

    /**
     * 批量进行反向地理编码，并输出到文件
     */
    public static void processRegeo() {
        File inputFile = new File("C:\\Users\\Guan\\dataset\\brightkite\\lab9\\pois\\onlyPosition_test.txt");
        File outputFile = new File("C:\\Users\\Guan\\dataset\\brightkite\\lab9\\pois\\poi_test.txt");

        List<String> inputList = new ArrayList<>(210000);


        //输入
        FileManager.inputFile(inputFile, new FileManager.InputListener() {
            @Override
            public void input(String line) {
                inputList.add(line);
            }
        });


        FileManager.outputFile(outputFile, new FileManager.OutputListener() {
            @Override
            public void output(BufferedWriter bufferedWriter) {
                try {
                    for (String s : inputList) {
                        String[] ss = s.split("\t");
                        String newLine = s + "\t" + GeoUtil.regeoBaidu(ss[1], ss[2]).toPois();
                        bufferedWriter.write(newLine);
                        bufferedWriter.newLine();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, false);

    }

}
