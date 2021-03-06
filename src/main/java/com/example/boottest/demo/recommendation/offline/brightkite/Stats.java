package com.example.boottest.demo.recommendation.offline.brightkite;

import com.example.boottest.demo.recommendation.offline.FileManager;
import com.example.boottest.demo.recommendation.offline.brightkite.model.CheckIn;
import com.example.boottest.demo.recommendation.offline.brightkite.model.RatingHolder;
import com.example.boottest.demo.recommendation.offline.brightkite.model.RecordHolder;
import com.example.boottest.demo.recommendation.offline.model.Item;
import com.example.boottest.demo.recommendation.offline.model.User;

import java.io.File;

/**
 * @author Guan
 * @date Created on 2019/3/19
 */
public class Stats {

    private static String filePath = "C:\\Users\\Guan\\dataset\\brightkite\\Brightkite_totalCheckins.txt";
    private static String filepath_20POI_lab9 = "C:\\Users\\Guan\\dataset\\brightkite\\lab9\\Brightkite_totalCheckins_20POI.txt";

    private static String filePath_100 = "C:\\Users\\Guan\\dataset\\brightkite\\Brightkite_totalCheckins_100.txt";
    private static String filepath_20POI = "C:\\Users\\Guan\\dataset\\brightkite\\Brightkite_totalCheckins_20POI.txt";
    private static String filePath_Count = "C:\\Users\\Guan\\dataset\\brightkite\\Brightkite_totalCheckins_count.txt";
    private static String filePath_Rating = "C:\\Users\\Guan\\dataset\\brightkite\\Brightkite_totalCheckins_rating.txt";

    private static String filepath_UserItemRating = "C:\\Users\\Guan\\dataset\\brightkite\\lab8\\20POI_UserItemRating.txt";

    /**
     * 统计有效记录总数
     */
    public static void inputTotal() {
        RecordHolder recordHolder = new RecordHolder();

        File file = new File(filePath);
        FileManager.inputFile(file, new FileManager.InputListener() {
            @Override
            public void input(String line) {
                String[] ss = line.split("\t");
                if (ss.length != 5) {
                    System.out.println("错误数据：" + line);
                    return;
                }
                //纬度
                double latitude = Double.parseDouble(ss[2]);
                //经度
                double longitude = Double.parseDouble(ss[3]);
                if (latitude == 0.0 || longitude == 0.0 || "00000000000000000000000000000000".equals(ss[4])) {
                    return;
                }
                CheckIn checkIn = new CheckIn(ss[4], ss[1], longitude, latitude);
                User user = new User(ss[0]);
                recordHolder.put(user, checkIn);

            }
        });
//        recordHolder.refreshDataSet(100);
//        recordHolder.output(new File(filePath_100));
//        recordHolder.stats3();
//        recordHolder.stats4(new File(filePath_Rating),5);
        recordHolder.stats5(new File(filepath_20POI_lab9));

    }

    /**
     * 计算用户项目评分
     */
    public static void countUserItemRating() {
        RatingHolder ratingHolder = new RatingHolder();
        File file = new File(filepath_20POI);
        FileManager.inputFile(file, new FileManager.InputListener() {
            @Override
            public void input(String line) {
                String[] ss = line.split("\t");
                if (ss.length != 5) {
                    System.out.println("错误数据：" + line);
                    return;
                }

                User user = new User(ss[0]);
                Item item = new Item(ss[4]);
                ratingHolder.put(user, item);

            }
        });

        ratingHolder.output(new File(filepath_UserItemRating));

    }

}
