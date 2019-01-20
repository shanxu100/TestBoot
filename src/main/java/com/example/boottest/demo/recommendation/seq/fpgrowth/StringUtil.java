package com.example.boottest.demo.recommendation.seq.fpgrowth;

import java.util.List;

/**
 * Created by DJH on 2018/10/23
 */
public class StringUtil {

    private StringUtil() {
        throw new RuntimeException();
    }

    public static boolean isEmpty(String string) {
        return string == null || "".equals(string);
    }

    /**
     * 将list<String> 转化为String
     *
     * @param list
     * @return String 字符串
     */
    public static String list2Str(List<String> list) {
        String res = "";
        for (int i = 0; i < list.size(); i++) {
            res += list.get(i);
        }
        return res;
    }

    /**
     * 构建next数组，KMP算法的辅助函数
     *
     * @param str
     * @return next数组
     */
    public static int[] next(String str) {
        char[] t = str.toCharArray();
        int[] next = new int[t.length];
        next[0] = -1;
        int i = 0;
        int j = -1;
        while (i < t.length - 1) {
            if (j == -1 || t[i] == t[j]) {
                i++;
                j++;
                if (t[i] != t[j]) {
                    next[i] = j;
                } else {
                    next[i] = next[j];
                }
            } else {
                j = next[j];
            }
        }
        return next;
    }

    /**
     * KMP匹配字符串
     *
     * @param
     * @param
     * @return 若匹配成功，返回下标，否则返回-1
     */
    public static boolean KMP_Index(String str, String pattern) {
        int[] next = next(pattern);
        char[] t = pattern.toCharArray();
        char[] s = str.toCharArray();
        int i = 0;
        int j = 0;
        while (i <= s.length - 1 && j <= t.length - 1) {
            if (j == -1 || s[i] == t[j]) {
                i++;
                j++;
            } else {
                j = next[j];
            }
        }
        if (j < t.length) {
            return false;
        } else
            return true; // 返回模式串在主串中的头下标
    }
}
