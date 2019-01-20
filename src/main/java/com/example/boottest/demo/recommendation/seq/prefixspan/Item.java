package com.example.boottest.demo.recommendation.seq.prefixspan;

public class Item {
    public int key;

    /**
     * minimum
     */
    public double mis;

    public int frequence;

    /**
     * true:frequent false:unfrequent
     */
    public boolean flag;

    public Item() {
        this.key = 0;
        this.mis = 0;
        this.frequence = 0;
        this.flag = false;
    }

    public Item(int arg0, double arg1, int arg2, boolean arg3) {
        this.key = arg0;
        this.mis = arg1;
        this.frequence = arg2;
        this.flag = arg3;
    }

    public Item copyItem() {
        Item item = new Item();
        item.flag = this.flag;
        item.frequence = this.frequence;
        item.flag = this.flag;
        item.key = this.key;
        return item;
    }
}
