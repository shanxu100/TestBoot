package com.example.boottest.demo.recommendation.offline;

import com.example.boottest.demo.recommendation.offline.math.SequenceUtil;
import com.example.boottest.demo.recommendation.offline.math.TimeUtil;
import com.example.boottest.demo.recommendation.offline.model.Item;
import com.example.boottest.demo.recommendation.offline.model.Rating;
import com.example.boottest.demo.recommendation.offline.model.User;

import java.util.Map;

/**
 * @author Guan
 * @date Created on 2019/3/13
 */
public class ContextSimilaryManager {


    public static double getContextSimilary(User u1, User u2) {
        Map<Item, Rating> mapU1 = UserItemMatrixManager.getRatingData(u1);
        Map<Item, Rating> mapU2 = UserItemMatrixManager.getRatingData(u2);

        if (mapU1.size() == 0 || mapU2.size() == 0) {
            return 0;
        }

        StringBuilder sb1 = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();
        for (Map.Entry<Item, Rating> entry : mapU1.entrySet()) {
            sb1.append(TimeUtil.getTimeSegment(entry.getValue().getTimestamp()));
        }
        for (Map.Entry<Item, Rating> entry : mapU2.entrySet()) {
            sb2.append(TimeUtil.getTimeSegment(entry.getValue().getTimestamp()));
        }
        int numerator = SequenceUtil.getLCS(sb1.toString(), sb2.toString());
        int denominator = mapU1.size() > mapU2.size() ? mapU1.size() : mapU2.size();
        return numerator / denominator;

    }
}
