package com.example.boottest.demo.recommendation.offline.math;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Guan
 * @date Created on 2019/3/10
 */
public class CollectionUtil {

    /**
     * 寻找两个集合的交集数量
     *
     * @param s1
     * @param s2
     * @return
     */
    public static int intersectionCount(Set s1, Set s2) {

        if (s1.size() == 0 || s2.size() == 0) {
            return 0;
        }

        int count = 0;
        for (Object object : s1) {
            count = s2.contains(object) ? count + 1 : count;
        }
        return count;
    }

    /**
     * 计算两个集合的并集
     *
     * @param s1
     * @param s2
     * @return
     */
    public static int setCount(Set s1, Set s2) {
        int count = s1.size();
        for (Object o : s2) {
            if (s1.contains(o)) {
                continue;
            }
            count++;
        }
        return count;
    }
}
