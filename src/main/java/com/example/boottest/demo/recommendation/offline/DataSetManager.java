package com.example.boottest.demo.recommendation.offline;

import com.example.boottest.demo.recommendation.offline.model.Item;
import com.example.boottest.demo.recommendation.offline.model.Rating;
import com.example.boottest.demo.recommendation.offline.model.User;

import java.io.File;
import java.util.*;

/**
 * @author Guan
 * @date Created on 2019/3/9
 */
public class DataSetManager {


    private static Map<User, Map<Item, Rating>> map = new HashMap<>();


    /**
     * 创建训练集合测试集
     */
    public static void createTrainAndTestSet(File file, File trainSetFile, File testSetFile, String separator) {

        List<String> list = FileManager.inputFile(file);
        for (String line : list) {
            String[] ss = line.split(separator);
            User user = new User(ss[0]);
            Item item = new Item(ss[1]);
            Rating rating = new Rating(user, item, Double.parseDouble(ss[2]));
            put(user, item, rating);
        }
        System.out.println("原始数据集读取成功....");
        output(trainSetFile, testSetFile, separator);

    }

    private static void put(User user, Item item, Rating rating) {

        if (map.containsKey(user)) {
            map.get(user).put(item, rating);
            return;
        }
        Map<Item, Rating> hashMap = new HashMap<>();
        hashMap.put(item, rating);
        map.put(user, hashMap);
    }

    private static void output(File trainSetFile, File testSetFile, String separator) {
        Iterator<User> iterator = map.keySet().iterator();

        Map<User, List<Rating>> trainMap = new HashMap<>(1024);
        Map<User, List<Rating>> testMap = new HashMap<>(1024);

        while (iterator.hasNext()) {

            //对于每一个用户，20%测试集，80%训练集

            User user = iterator.next();
            System.out.println("处理用户:" + user.getUserId());

            Map<Item, Rating> itemRatingMap = map.get(user);
            int totalSize = itemRatingMap.size();
            int trainSize = (int) (totalSize * 0.8);
            List<Rating> trainList = new ArrayList<>(trainSize);
            int testSize = totalSize - trainSize;
            List<Rating> testList = new ArrayList<>(testSize);

            Item[] items = itemRatingMap.keySet().toArray(new Item[0]);

            Integer[] randoms = genFakeRandomNums(testSize, items.length);
            System.out.print("随机数个数" + randoms.length);


            //产生测试集
            for (int i : randoms) {
                Item item = items[i];
                testList.add(itemRatingMap.get(item));
                itemRatingMap.remove(item);
            }
            System.out.println();
            //剩余的都是训练集
            for (Item item : itemRatingMap.keySet()) {
                trainList.add(itemRatingMap.get(item));
            }

            trainMap.put(user, trainList);
            testMap.put(user, testList);

        }

        FileManager.outputFile(trainSetFile, trainMap, separator);
        FileManager.outputFile(testSetFile, testMap, separator);

    }


    /**
     * 产生size个0-range范围内的随机数
     *
     * @param size
     * @param range
     * @return
     */
    private static Integer[] genRandomNums(int size, int range) {
        int[] result = new int[size];
        Random random = new Random();
        Set<Integer> set = new HashSet<>();
        while (set.size() < size) {
            set.add(random.nextInt(range));
        }
        return set.toArray(new Integer[0]);
    }

    /**
     * 产生size个0-range范围内的随机数
     *
     * @param size
     * @param range
     * @return
     */
    private static Integer[] genFakeRandomNums(int size, int range) {
        Integer[] result = new Integer[size];
        for (int i = 0; i < size; i++) {
            result[i] = range - 1 - i;
        }
        return result;
    }

}
