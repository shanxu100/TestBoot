package com.example.boottest.demo.recommendation.offline.brightkite.slopeone;

import com.example.boottest.demo.recommendation.offline.FileManager;
import com.example.boottest.demo.recommendation.offline.model.Item;
import com.example.boottest.demo.recommendation.offline.model.Rating;
import com.example.boottest.demo.recommendation.offline.model.User;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Guan
 * @date Created on 2019/4/12
 */
public class SlopeOneDataSet {

    private static Map<Item, Map<User, Rating>> map = new HashMap<>();


    private static void put(User user, Item item, Rating rating) {

        if (map.containsKey(item)) {
            map.get(item).put(user, rating);
            return;
        }
        Map<User, Rating> hashMap = new HashMap<>();
        hashMap.put(user, rating);
        map.put(item, hashMap);
    }

    /**
     * 输入文件，将数据插入到map中
     *
     * @param file
     */
    public static void input(File file, String separator) {

        List<String> list = FileManager.inputFile(file);
        for (String line : list) {
            String[] ss = line.split(separator);
            User user = new User(ss[0]);
            Item item = new Item(ss[1]);
            Rating rating = null;
            if (ss.length == 3) {
                rating = new Rating(user, item, Double.parseDouble(ss[2]));
            } else if (ss.length == 4) {
                rating = new Rating(user, item, Double.parseDouble(ss[2]), Long.parseLong(ss[3]));
            }
            put(user, item, rating);
        }

    }

    public static Map<User, Rating> get(Item item) {
        if (map.containsKey(item)) {
            return map.get(item);
        } else {
            return null;
        }
    }

    public static Set<Item> getAllItem() {
        return map.keySet();
    }

}
