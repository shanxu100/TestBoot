package com.example.boottest.demo.recommendation.offline.brightkite.model;

import com.example.boottest.demo.recommendation.offline.FileManager;
import com.example.boottest.demo.recommendation.offline.model.User;

import java.io.BufferedWriter;
import java.io.File;
import java.util.*;

/**
 * @author Guan
 * @date Created on 2019/3/19
 */
public class RecordHolder {

    private Map<User, List<CheckIn>> map = new HashMap<>();


    public void put(User user, CheckIn checkIn) {

        if (map.containsKey(user)) {
            map.get(user).add(checkIn);
        } else {
            List<CheckIn> list = new ArrayList<>(1024);
            list.add(checkIn);
            map.put(user, list);
        }
    }

    /**
     * 过滤掉签到次数小于等于阈值的用户
     *
     * @param threshold
     */
    public void refreshDataSet(int threshold) {

        System.out.println("开始过滤---refreshDataSet");
        Iterator<Map.Entry<User, List<CheckIn>>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<User, List<CheckIn>> entry = iterator.next();
            if (entry.getValue().size() <= threshold) {
                iterator.remove();
            }
        }
    }

    public void output(File outputFile) {
        System.out.println("输出文件---" + outputFile.getPath());
        FileManager.outputFile(outputFile, new FileManager.OutputListener() {
            @Override
            public void output(BufferedWriter bufferedWriter) {

                try {
                    for (Map.Entry<User, List<CheckIn>> entry : map.entrySet()) {

                        User user = entry.getKey();
                        List<CheckIn> list = entry.getValue();
                        for (CheckIn checkIn : list) {
                            StringBuilder sb = new StringBuilder(user.getUserId()).append("\t")
                                    .append(checkIn.getTime()).append("\t")
                                    .append(checkIn.getLatitude()).append("\t")
                                    .append(checkIn.getLongitude()).append("\t")
                                    .append(checkIn.getId());
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
     * 统计不同签到范围的人数和记录数占比
     * 例如：
     * 总人数：51406.0
     * 总记录数：4747281.0
     * 0-1*stepLength：   人数=38387 %=0.7467416254911878------记录数=411613 %=0.08670500018852897
     * 1*stepLength-2stepLength：   人数=4537 %=0.08825817997899078------记录数=325890 %=0.06864771645074307
     * 2stepLength-：   人数=8482 %=0.16500019452982143------记录数=4009778 %=0.8446472833607279
     */
    public void stats() {
        int stepLength = 50;
        double num = 0.0;
        int num0_10 = 0;
        int num10_20 = 0;
        int num20_30 = 0;
        int num30_40 = 0;
        int num40_50 = 0;
        int num50_ = 0;

        double people = 0.0;
        int people0_10 = 0;
        int people10_20 = 0;
        int people20_30 = 0;
        int people30_40 = 0;
        int people40_50 = 0;
        int people50_ = 0;

        people = map.size();
        for (Map.Entry<User, List<CheckIn>> entry : map.entrySet()) {
            List<CheckIn> list = entry.getValue();

            int size = list.size();
            num += size;
            if (size > 0 && size <= stepLength) {
                num0_10 += size;
                people0_10++;
            } else if (size > stepLength && size <= 2 * stepLength) {
                num10_20 += size;
                people10_20++;
            } else {
                num20_30 += size;
                people20_30++;
            }

        }

        System.out.println("总人数：" + people);
        System.out.println("总记录数：" + num);
        System.out.println("0-1*stepLength：   人数=" + people0_10 + " %=" + people0_10 / people + "------记录数=" + num0_10 + " %=" + num0_10 / num);
        System.out.println("1*stepLength-2stepLength：   人数=" + people10_20 + " %=" + people10_20 / people + "------记录数=" + num10_20 + " %=" + num10_20 / num);
        System.out.println("2stepLength-：   人数=" + people20_30 + " %=" + people20_30 / people + "------记录数=" + num20_30 + " %=" + num20_30 / num);
//        System.out.println("3stepLength-4stepLength：   人数=" + people30_40 + " %=" + people30_40 / people + "------记录数=" + num30_40 + " %=" + num30_40 / num);
//        System.out.println("4stepLength-5stepLength：   人数=" + people40_50 + " %=" + people40_50 / people + "------记录数=" + num40_50 + " %=" + num40_50 / num);
//        System.out.println("5stepLength_：   人数=" + people50_ + " %=" + people50_ / people + "------记录数=" + num50_ + " %=" + num50_ / num);

    }


    /**
     * 统计最大签到次数的范围
     */
    public void stats2() {
        int stepLength = 10;
        double num = 0.0;
        int num0_1stepLength = 0;
        int num1_2stepLength = 0;
        int num2_3stepLength = 0;
        int num3_4stepLength = 0;
        int num4_5stepLength = 0;
        int num5_ = 0;

        for (Map.Entry<User, List<CheckIn>> entry : map.entrySet()) {

            if (entry.getValue().size() < 100) {
                continue;
            }
            num++;
            int maxCount = getMaxCount(entry.getKey(), entry.getValue());
            if (maxCount >= 0 && maxCount < stepLength) {
                num0_1stepLength++;
            } else if (maxCount >= stepLength && maxCount < 2 * stepLength) {
                num1_2stepLength++;
            } else if (maxCount >= 2 * stepLength && maxCount < 3 * stepLength) {
                num2_3stepLength++;
            } else if (maxCount >= 3 * stepLength && maxCount < 4 * stepLength) {
                num3_4stepLength++;
            } else if (maxCount >= 4 * stepLength && maxCount < 5 * stepLength) {
                num4_5stepLength++;
            } else {
                num5_++;
            }

        }
        System.out.println("总人数：" + num);
        System.out.println("0-1*stepLength：   最大签到次数=" + num0_1stepLength + " %=" + num0_1stepLength / num);
        System.out.println("1*stepLength-2stepLength：   最大签到次数=" + num1_2stepLength + " %=" + num1_2stepLength / num);
        System.out.println("2stepLength-3：   最大签到次数=" + num2_3stepLength + " %=" + num2_3stepLength / num);
        System.out.println("3stepLength-4：   最大签到次数=" + num3_4stepLength + " %=" + num3_4stepLength / num);
        System.out.println("4stepLength-5：   最大签到次数=" + num4_5stepLength + " %=" + num4_5stepLength / num);
        System.out.println("5_：   最大签到次数=" + num5_ + " %=" + num5_ / num);


    }

    /**
     * 签到地点总数
     */
    public void stats3() {

        Set<String> set = new HashSet<>(300000);
        for (Map.Entry<User, List<CheckIn>> entry : map.entrySet()) {
            List<CheckIn> list = entry.getValue();
            for (CheckIn checkIn : list) {
                set.add(checkIn.getId());
            }

        }
        System.out.println("签到地点总数：" + set.size());
    }

    /**
     * 统计每一位用户的对地点的访问次数
     */
    public void stats4(File file,int maxCount) {
        System.out.println("开始统计次数------");
        Map<User, Map<String, CheckInCount>> countMap = new HashMap<>(9000);
        for (Map.Entry<User, List<CheckIn>> entry : map.entrySet()) {
            User user = entry.getKey();
            List<CheckIn> list = entry.getValue();
            Map<String, CheckInCount> subMap = new HashMap<>();
            for (CheckIn checkIn : list) {
                if (subMap.containsKey(checkIn.getId())) {
                    subMap.get(checkIn.getId()).countAddAdd(maxCount);
                } else {
                    CheckInCount checkInCount = new CheckInCount(checkIn.id, checkIn.getLongitude(), checkIn.latitude, 1);
                    subMap.put(checkIn.getId(), checkInCount);
                }
            }
            countMap.put(user, subMap);
        }
        System.out.println("开始写入文件------");
        FileManager.outputFile(file, new FileManager.OutputListener() {
            @Override
            public void output(BufferedWriter bufferedWriter) {
                try {
                    for (Map.Entry<User, Map<String, CheckInCount>> entry : countMap.entrySet()) {
                        User user = entry.getKey();
                        Map<String, CheckInCount> subMap = entry.getValue();
                        for (Map.Entry<String, CheckInCount> subEntry : subMap.entrySet()) {
                            CheckInCount checkInCount = subEntry.getValue();
                            StringBuilder sb = new StringBuilder(user.getUserId()).append("\t")
                                    .append(checkInCount.getId()).append("\t")
                                    .append(checkInCount.getCount()).append("\t")
                                    .append(checkInCount.getLatitude()).append("\t")
                                    .append(checkInCount.getLongitude()).append("\t");
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
     * 找到list中次数最多的元素及其个数
     *
     * @param user
     * @param list
     * @return
     */
    private int getMaxCount(User user, List<CheckIn> list) {

        if (list.size() == 0) {
            return 0;
        }

        Map<String, Integer> map = new HashMap<String, Integer>();
        for (CheckIn checkIn : list) {
            if (map.containsKey(checkIn.getId())) {
                int count = map.get(checkIn.getId());
                map.put(checkIn.getId(), count + 1);
            } else {
                map.put(checkIn.getId(), 1);
            }
        }

        int max = 0;
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            max = entry.getValue() > max ? entry.getValue() : max;
            System.out.println(user.getUserId() + "\t" + entry.getKey() + "\t" + entry.getValue());
        }
        return max;
    }

}
