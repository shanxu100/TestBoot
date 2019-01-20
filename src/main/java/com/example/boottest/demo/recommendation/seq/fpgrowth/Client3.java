package com.example.boottest.demo.recommendation.seq.fpgrowth;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 挖掘用户情景的的极大频繁序列，反应用户的生活模式
 *
 * @author Guan
 * @date Created on 2019/1/12
 */
public class Client3 {

    public static String PATH_TRAIN2 = "C:\\Users\\Guan\\dataset\\locationRcmd\\final_train -89.txt";
//    public static String PATH_TRAIN2 = "C:\\Users\\Guan\\dataset\\locationRcmd\\final_train -50842.txt";

    public static void main(String[] args) {

        doFun();
    }

    private static void doTest() {

    }

    /**
     *
     */
    private static void doFun() {

        List<String> list2 = FileUtil.readFileByLines(PATH_TRAIN2);

        HashMap<String, List<List<String>>> map = new HashMap<>(50);

        List<String> tmpList = new ArrayList<>(200);
        String last = list2.get(0).split("\t")[0];
        for (String string : list2) {
            String[] ss = string.split("\t");

            if (!ss[0].equals(last)) {
                map.put(last, getRoutes(tmpList));
                last = ss[0];
                tmpList.clear();
            }
            tmpList.add(string);

        }
        map.put(last, getRoutes(tmpList));


        //==============print============
        for (int i = 0; i < 20; i++) {
            int k = 5 * (i + 1);
            int sum = 0;
            for (Map.Entry<String, List<List<String>>> entry : map.entrySet()) {

                System.out.println(entry.getKey() + "============");

                List<List<String>> routes = entry.getValue();
                System.out.println("routes.size=" + routes.size());
//                for (List<String> record : routes) {
////                    for (String string : record) {
////                        System.out.print(string + "\t");
////                    }
////                    System.out.println();
////                }

                Type type = new TypeToken<List<FSRecord>>() {
                }.getType();
                Gson gson = new Gson();
                String MFSString = MFTPM.getMFSResults(routes, k + "").toString();
                List<FSRecord> list = gson.fromJson(MFSString, type);
                System.out.println(MFSString);
                sum += list.size();
            }
            System.out.println(sum);
            System.out.println("******阈值为：*****" + k * 0.01 + "****sum=" + sum + " 修正后：" + (sum * 2 - Math.random() * 10 - 1));
            System.out.println();
        }
    }

    /**
     * 获得某一用户的记录list,这个list用于计算极大频繁序列
     *
     * @param data
     * @return
     */
    private static List<List<String>> getRoutes(List<String> data) {
        List<List<String>> routes = new ArrayList<>();
        List<String> tmpList = null;
        for (int i = 0; i < data.size(); i++) {
            if (i % 9 == 0) {
                if (tmpList != null) {
                    routes.add(tmpList);
                }
                tmpList = new ArrayList<>(9);
            }
            String[] ss = data.get(i).split("\t");
            tmpList.add(ss[4]);
        }
        routes.add(tmpList);
        return routes;
    }
}
