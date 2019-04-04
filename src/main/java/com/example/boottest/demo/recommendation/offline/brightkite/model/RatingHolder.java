package com.example.boottest.demo.recommendation.offline.brightkite.model;

import com.example.boottest.demo.recommendation.offline.FileManager;
import com.example.boottest.demo.recommendation.offline.model.Item;
import com.example.boottest.demo.recommendation.offline.model.User;

import java.io.BufferedWriter;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Guan
 * @date Created on 2019/4/4
 */
public class RatingHolder {
    private Map<User, Map<Item, Integer>> map = new HashMap<>();

    public void put(User user, Item item) {
        if (map.containsKey(user)) {
            Map<Item, Integer> itemMap = map.get(user);
            int count = itemMap.containsKey(item) ? itemMap.get(item) + 1 : 1;
            itemMap.put(item, count);
        } else {
            Map<Item, Integer> itemMap = new HashMap<>(64);
            itemMap.put(item, 1);
            map.put(user, itemMap);
        }
    }

    /**
     * 生成User-Item-Rating文件
     * @param outputFile
     */
    public void output(File outputFile) {
        System.out.println("输出文件---" + outputFile.getPath());
        FileManager.outputFile(outputFile, new FileManager.OutputListener() {
            @Override
            public void output(BufferedWriter bufferedWriter) {

                try {
                    for (Map.Entry<User, Map<Item, Integer>> entry : map.entrySet()) {

                        User user = entry.getKey();
                        Map<Item, Integer> itemMap = entry.getValue();
                        for (Map.Entry<Item, Integer> subEntry : itemMap.entrySet()) {
                            Item item = subEntry.getKey();
                            int count = subEntry.getValue();
                            StringBuilder sb = new StringBuilder(user.getUserId()).append("\t")
                                    .append(item.getItemId()).append("\t")
                                    .append(toRating(count));
                            bufferedWriter.write(sb.toString());
                            bufferedWriter.newLine();
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, false);
    }

    /**
     * 评分转换规则
     *
     * @param count
     * @return
     */
    private int toRating(int count) {
        if (count > 5) {
            return 5;
        } else {
            return count;
        }
    }

}
