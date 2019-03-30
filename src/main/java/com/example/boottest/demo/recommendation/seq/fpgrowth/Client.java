package com.example.boottest.demo.recommendation.seq.fpgrowth;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 经过多组实验求得不同的极大频繁序列，然后确定极大频繁序列的阈值
 *
 * @author Guan
 * @date Created on 2019/1/4
 */
public class Client {
    public static void main(String[] args) {

        List<List<String>> routes = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            int times = (int) (Math.random() * 50);
            List<String> record = new ArrayList<>();
            for (int j = 0; j < times; j++) {
                int random = (int) (Math.random() * 10);
                random = random == 0 ? random + 1 : random;
                record.add(random + "");
            }
//            routes.add(record);
        }

        //
        //阈值为：0.4
        //[{"freq_seq":"14->6->9","id":1,"frequency":4},
        // {"freq_seq":"40->4->41","id":2,"frequency":3}]
        String[][] data = {
                {"12", "14", "6", "9"},
                {"14", "6", "9"},
                {"40", "4", "41", "14", "6", "9"},
                {"40", "4", "41"},
                {"31", "32", "14", "6", "9"},
                {"40", "4", "41", "6", "42"},
                {"41", "6", "42"},
        };
//        String[][] data = {
//                {"1", "2", "3", "4"},
//                {"1", "3", "4", "3"},
//                {"1", "2", "3", "4"},
//                {"1", "3", "5"},
//                {"4", "5"},

//        };

        for (String[] ss : data) {
            routes.add(Arrays.asList(ss));
        }


        //==============print============
        for (List<String> record : routes) {

            for (String string : record) {
                System.out.print(string + "\t");
            }
            System.out.println();
        }
        System.out.println("=============================");

        System.out.println(MFTPM.getMFSResults(routes, 3 + ""));

//        for (int i = 0; i < 20; i++) {
//            int k = 5 * (i + 1);
//            System.out.println("阈值为：" + k * 0.01);
//            System.out.println(MFTPM.getMFSResults(routes, k + ""));
//
//        }


    }
}
