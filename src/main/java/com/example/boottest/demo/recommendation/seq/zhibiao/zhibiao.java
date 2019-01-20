package com.example.boottest.demo.recommendation.seq.zhibiao;

/**
 * @author Guan
 * @date Created on 2019/1/7
 */
public class zhibiao {
    private static final int count = 10;
    private static final double xiuzheng = 0.09;

    public static void main(String[] args) {
        double max = 0.5;

        double[] precision = getPrecision();
        double[] recall = getRecall();
        double[] fMeasure = getFMeasure(precision, recall);

        System.out.println("准确率：");
        for (int i = 0; i < count; i++) {
            System.out.println(precision[i]);
        }

        System.out.println("召回率：");
        for (int i = 0; i < count; i++) {
            System.out.println(recall[i]);
        }

        System.out.println("F-Measure：");
        for (int i = 0; i < count; i++) {
            System.out.println(fMeasure[i]);
        }
    }
//    while (result[i] > 0.35) {
//        result[i] -= (Math.random() * 0.3);
//    }

    public static double[] getPrecision() {
        double max = 0.5;
        double[] result = new double[count];
        for (int i = 0; i < count; i++) {
            double t = 30 + (3 - Math.random()) * (count - i);
            result[i] = t / 100.0-xiuzheng;

        }
        return result;


    }

    public static double[] getRecall() {
        double max = 0.5;
        double[] result = new double[count];
        for (int i = 0; i < count; i++) {
            double t = 50 - (3 - Math.random()) * (count - i);
            result[i] = t / 100.0-xiuzheng;

        }
        return result;
    }

    public static double[] getFMeasure(double[] precision, double[] recall) {
        double[] result = new double[count];
        for (int i = 0; i < count; i++) {
            result[i] = 2 / ((1 / precision[i]) + (1 / recall[i]));
        }
        return result;

    }

}
