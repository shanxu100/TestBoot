package com.example.boottest.demo.recommendation.offline.math;

import java.util.Stack;

/**
 * @author Guan
 * @date Created on 2019/3/13
 */
public class SequenceUtil {

    public static int getLCS(String x, String y) {

        char[] s1 = x.toCharArray();
        char[] s2 = y.toCharArray();
        //此处的棋盘长度要比字符串长度多加1，需要多存储一行0和一列0
        int[][] array = new int[x.length() + 1][y.length() + 1];

        for (int j = 0; j < array[0].length; j++) {
            //第0行第j列全部赋值为0
            array[0][j] = 0;
        }
        for (int i = 0; i < array.length; i++) {
            //第i行，第0列全部为0
            array[i][0] = 0;
        }

        for (int m = 1; m < array.length; m++) {
            //利用动态规划将数组赋满值
            for (int n = 1; n < array[m].length; n++) {
                if (s1[m - 1] == s2[n - 1]) {
                    //动态规划公式一
                    array[m][n] = array[m - 1][n - 1] + 1;
                } else {
                    //动态规划公式二
                    array[m][n] = max(array[m - 1][n], array[m][n - 1]);
                }
            }
        }
        Stack stack = new Stack();
        int i = x.length() - 1;
        int j = y.length() - 1;

        while ((i >= 0) && (j >= 0)) {
            if (s1[i] == s2[j]) {
                //字符串从后开始遍历，如若相等，则存入栈中
                stack.push(s1[i]);
                i--;
                j--;
            } else {
                if (array[i + 1][j] > array[i][j + 1]) {
                    //如果字符串的字符不同，则在数组中找相同的字符，注意：数组的行列要比字符串中字符的个数大1，因此i和j要各加1
                    j--;
                } else {
                    i--;
                }
            }
        }

//        while (!stack.isEmpty()) {
//            //打印输出栈正好是正向输出最大的公共子序列
//            System.out.print(stack.pop());
//        }
        return stack.size();
    }

    public static int max(int a, int b) {//比较(a,b)，输出大的值
        return (a > b) ? a : b;
    }

}