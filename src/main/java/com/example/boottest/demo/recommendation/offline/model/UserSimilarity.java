package com.example.boottest.demo.recommendation.offline.model;

import java.util.Objects;

/**
 * @author Guan
 * @date Created on 2019/3/6
 */
public class UserSimilarity implements Comparable<UserSimilarity> {
    private User u1;
    private User u2;
    private double similarity;

    public UserSimilarity(User u1, User u2, double similarity) {
        this.u1 = u1;
        this.u2 = u2;
        this.similarity = similarity;
    }

    public User getU1() {
        return u1;
    }

    public void setU1(User u1) {
        this.u1 = u1;
    }

    public User getU2() {
        return u2;
    }

    public void setU2(User u2) {
        this.u2 = u2;
    }

    public double getSimilarity() {
        return similarity;
    }

    public void setSimilarity(double similarity) {
        this.similarity = similarity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserSimilarity that = (UserSimilarity) o;
        return Double.compare(that.similarity, similarity) == 0 &&
                Objects.equals(u1, that.u1) &&
                Objects.equals(u2, that.u2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(u1, u2, similarity);
    }

    @Override
    public int compareTo(UserSimilarity o) {
        //为了在TreeSet或TreeMap中倒序排列
        if (similarity > o.similarity) {
            return -1;
        } else if (similarity < o.similarity) {
            return 1;
        }
        return 0;
    }

    @Override
    public String toString() {
        return "UserSimilarity{" +
                "u1=" + u1.getUserId() +
                ", u2=" + u2.getUserId() +
                ", similarity=" + similarity +
                '}';
    }
}
