package com.example.boottest.demo.recommendation.seq.gsp;


import java.util.ArrayList;

/**
 * 序列中的子项集
 *
 * @author lyq
 */
public class ItemSet {
    /**
     * 项集中保存的是数字项数组
     */
    private ArrayList<Integer> items;

    public ItemSet(String[] itemStr) {
        items = new ArrayList<>();
        for (String s : itemStr) {
            items.add(Integer.parseInt(s));
        }
    }

    public ItemSet(int[] itemNum) {
        items = new ArrayList<>();
        for (int num : itemNum) {
            items.add(num);
        }
    }

    public ItemSet(ArrayList<Integer> itemNum) {
        this.items = itemNum;
    }

    public ArrayList<Integer> getItems() {
        return items;
    }

    public void setItems(ArrayList<Integer> items) {
        this.items = items;
    }

    /**
     * 判断2个项集是否相等
     *
     * @param itemSet 比较对象
     * @return
     */
    public boolean compareIsSame(ItemSet itemSet) {
        boolean result = true;

        if (this.items.size() != itemSet.items.size()) {
            return false;
        }

        for (int i = 0; i < itemSet.items.size(); i++) {
            //在用两个Integer对象比较数值的话，如果是整型值的话最好调用intValue方法来得到一个int类型的值.
            // 当然也可将其转变为floatValue()或longValue()比较
            if (this.items.get(i).intValue()
                    != itemSet.items.get(i).intValue()) {
                // 只要有值不相等，直接算作不相等
                result = false;
                break;
            }
        }

        return result;
    }

    /**
     * 拷贝项集中同样的数据一份
     *
     * @return
     */
    public ArrayList<Integer> copyItems() {
        ArrayList<Integer> copyItems = new ArrayList<>();

        for (int num : this.items) {
            copyItems.add(num);
        }

        return copyItems;
    }
}

