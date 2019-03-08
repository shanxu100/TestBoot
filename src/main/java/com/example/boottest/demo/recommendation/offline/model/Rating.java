package com.example.boottest.demo.recommendation.offline.model;

import java.util.Objects;

/**
 * @author Guan
 * @date Created on 2019/3/6
 */
public class Rating implements Comparable<Rating> {
    private User user;
    private Item item;
    private double rating;

    public Rating(User user, Item item, double rating) {
        this.user = user;
        this.item = item;
        this.rating = rating;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rating rating1 = (Rating) o;
        return Double.compare(rating1.rating, rating) == 0 &&
                Objects.equals(user, rating1.user) &&
                Objects.equals(item, rating1.item);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, item, rating);
    }

    @Override
    public int compareTo(Rating o) {
        //为了在TreeSet或TreeMap中倒序排列
        if (this.rating > o.rating) {
            return -1;
        } else if (this.rating < o.rating) {
            return 1;
        }
        return 0;
    }


}
