package com.example.boottest.demo.recommendation.offline.model;

/**
 * @author Guan
 * @date Created on 2019/3/8
 */
public class Evaluation {

    private double precision;
    private double recall;
    private double fMeasure;

    public Evaluation(double precision, double recall, double fMeasure) {
        this.precision = precision;
        this.recall = recall;
        this.fMeasure = fMeasure;
    }

    public double getPrecision() {
        return precision;
    }

    public void setPrecision(double precision) {
        this.precision = precision;
    }

    public double getRecall() {
        return recall;
    }

    public void setRecall(double recall) {
        this.recall = recall;
    }

    public double getfMeasure() {
        return fMeasure;
    }

    public void setfMeasure(double fMeasure) {
        this.fMeasure = fMeasure;
    }

    @Override
    public String toString() {
        return "Evaluation{" +
                "precision=" + precision +
                ", recall=" + recall +
                ", fMeasure=" + fMeasure +
                '}';
    }

    /**
     * 格式化输出
     *
     * @param separator
     * @return
     */
    public String toFormattedString(String separator) {
        return precision + separator + recall + separator + fMeasure;
    }
}
