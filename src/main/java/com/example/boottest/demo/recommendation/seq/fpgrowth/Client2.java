package com.example.boottest.demo.recommendation.seq.fpgrowth;

import com.example.boottest.demo.recommendation.ctx.ContextConstant;
import com.example.boottest.demo.recommendation.geo.GeoUtil;
import com.example.boottest.demo.recommendation.model.GeoInfo;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Guan
 * @date Created on 2019/1/12
 */
public class Client2 {

    public static String PATH_TRAIN = "C:\\Users\\Guan\\dataset\\locationRcmd\\sub_train.txt";
    public static String PATH_TRAIN2 = "C:\\Users\\Guan\\dataset\\locationRcmd\\final_train.txt";

    public static String PATH_LOCATION = "C:\\Users\\Guan\\dataset\\locationRcmd\\mappedpoiinfo.txt";

    public static void main(String[] args) throws Exception {

        //加载经纬度
        List<String> list1 = FileUtil.readFileByLines(PATH_LOCATION);
        HashMap<String, JW> jwMap = new HashMap<>(list1.size());
        for (String string : list1) {
            String[] ss = string.split("\t");
            jwMap.put(ss[0], new JW(ss[1], ss[2]));
        }

        //加载训练子集
        List<String> list2 = FileUtil.readFileByLines(PATH_TRAIN);
        List<Record> records = new ArrayList<>();
        int count = 0;
        for (String string : list2) {
            String[] ss = string.split(",");
            if (ss[0].equals("50842")) {
                break;
            }
            JW jw = jwMap.get(ss[1]);
            Thread.sleep(50);
            GeoInfo geoInfo = GeoUtil.regeo(jw.latitude, jw.longitude);
            if (geoInfo == null) {
                continue;
            }
            List<GeoInfo.Aoi> aois = geoInfo.regeocode.aois;
            if (aois != null && aois.size() != 0) {
                String placeType = ContextConstant.getPlaceType(aois.get(0).type);
                //随机生成
                String contextId;
                if ((count % 9) < 3) {
                    contextId = ContextConstant.getContextId(placeType, "01");
                } else if ((count % 9) >= 6) {
                    contextId = ContextConstant.getContextId(placeType, "03");
                } else {
                    contextId = ContextConstant.getContextId(placeType, "02");
                }

                Record record = new Record(ss[0], ss[1], ss[2], placeType, contextId);
                records.add(record);
            }
            count++;
        }
        FileUtil.writeFile(PATH_TRAIN2, records);


    }


    private static class JW {
        String latitude;
        String longitude;

        public JW(String latitude, String longitude) {
            this.latitude = latitude;
            this.longitude = longitude;
        }
    }

    private static class Record {
        String userId;
        String itemId;
        String rating;
        String placeType;
        String contextId;


        public Record(String userId, String itemId, String rating, String placeType, String contextId) {
            this.userId = userId;
            this.itemId = itemId;
            this.rating = rating;
            this.placeType = placeType;
            this.contextId = contextId;
        }

        @Override
        public String toString() {
            return userId + '\t' + itemId + '\t' + rating + '\t' + placeType + '\t' + contextId + '\t';
        }
    }

}
