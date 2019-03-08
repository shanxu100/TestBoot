package com.example.boottest.demo.recommendation.offline;

import com.example.boottest.demo.recommendation.offline.model.Item;
import com.example.boottest.demo.recommendation.offline.model.Rating;
import com.example.boottest.demo.recommendation.offline.model.User;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * 用户项目评分矩阵
 *
 * @author Guan
 * @date Created on 2019/3/6
 */
public class UserItemMatrixManager {


    private static Map<User, Map<Item, Rating>> map = new HashMap<>();


    private static void put(User user, Item item, Rating rating) {

        if (map.containsKey(user)) {
            map.get(user).put(item, rating);
            return;
        }
        Map<Item, Rating> hashMap = new HashMap<>();
        hashMap.put(item, rating);
        map.put(user, hashMap);
    }

    /**
     * 输入文件，将数据插入到map中
     *
     * @param file
     */
    public static void input(File file, String separator) {

        try {
            // 建立一个对象，它把文件内容转成计算机能读懂的语言
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line = "";
            while ((line = br.readLine()) != null) {
                // 一次读入一行数据
                String[] ss = line.split(separator);
                User user = new User(ss[0]);
                Item item = new Item(ss[1]);
                Rating rating = new Rating(user, item, Double.parseDouble(ss[2]));
                put(user, item, rating);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * 获取指定用户的所有评分数据
     *
     * @param user
     * @return
     */
    public static Map<Item, Rating> getRatingData(User user) {
        if (map.containsKey(user)) {
            return map.get(user);
        }
        return new HashMap<>(0);
    }


    /**
     * 获取用户User对指定Item的评分数据
     *
     * @param user
     * @param item
     * @return
     */
    public static Rating getRatingData(User user, Item item) {
        if (map.containsKey(user) && map.get(user).containsKey(item)) {
            return map.get(user).get(item);
        }
        return null;
    }


    /**
     * 获得用户列表
     *
     * @return
     */
    public static Set<User> getAllUser() {
        return map.keySet();
    }

    /**
     * 根据用户Id获取User对象
     *
     * @param userId
     * @return
     */
    public static User getUser(String userId) {
        for (User user : map.keySet()) {
            if (user.getUserId().equals(userId)) {
                return user;
            }
        }
        return null;
    }
}
