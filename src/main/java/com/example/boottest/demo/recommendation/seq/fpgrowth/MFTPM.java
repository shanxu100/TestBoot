package com.example.boottest.demo.recommendation.seq.fpgrowth;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 生成极大频繁序列的类
 *
 * @author zengxi
 */
public class MFTPM {

    private static ArrayList<TreeNode> HeaderTable;      //频繁项表头
    private static MFSTree fstree;      //频繁序列树
    private static List<List<String>> mftss;    //极大频繁序列集
    private static List<List<String>> transRecords = new LinkedList<List<String>>();

    public static void setTransRecords(List<List<String>> transRecords) {
        MFTPM.transRecords = transRecords;
    }

    /**
     * 初始化函数
     */
    public static void init(List<List<String>> routes, String minSup) {
        transRecords = routes;
        fstree = new MFSTree();
        float Sup = 0.0f;
        Sup = Float.parseFloat(minSup);
        fstree.setMinSuport(Sup);   //设置最小支持度
        for (int i = 0; i < transRecords.size(); i++) {

        }

        mftss = new LinkedList<>();
        HeaderTable = fstree.buildHeaderTable(transRecords);     //生成1阶频繁项
        TreeNode treeRoot = fstree.buildFSTree(transRecords, HeaderTable);    //构建频繁序列树
    }

    /**
     * 将list<TreeNode>反转
     *
     * @param prePage 条件模式基
     * @return 反转后的序列树
     */
    public static List<TreeNode> invList(List<TreeNode> prePage) {
        List<TreeNode> res = null;
        if (prePage.size() > 0)
            res = new LinkedList<TreeNode>();
        for (int i = prePage.size() - 1; i >= 0; i--) {
            res.add(prePage.get(i));
        }
        return res;
    }

    /**
     * 将list<String>反转
     *
     * @param prePage
     * @return 反转后的序列
     */
    public static List<String> invStrList(List<String> prePage) {
        List<String> res = null;
        if (prePage.size() > 0)
            res = new LinkedList<String>();
        for (int i = prePage.size() - 1; i >= 00; i--) {
            res.add(prePage.get(i));
        }
        return res;
    }


    /**
     * 将path中所有项的“：数量”删去
     *
     * @param path 路径集
     * @return 处理后的路径集
     */
    public static List<String> trimSep(List<String> path) {
        List<String> res = new LinkedList<String>();
        for (int i = 0; i < path.size(); i++) {
            if (path.get(i).indexOf("=") != -1)
                res.add(path.get(i));
            else if (path.get(i).indexOf(":") != -1)
                res.add(path.get(i).substring(0, path.get(i).indexOf(":")));
        }
        return res;
    }

    /**
     * 找到1阶频繁序列集的所有频繁项的所有条件模式基，并将极大频繁序列添加到mftss中
     *
     * @param headerTable 1阶频繁序列集
     */
    public static void findPrePages(ArrayList<TreeNode> headerTable) {
        List<List<TreeNode>> prePageSet = null;
        for (int i = 0; i < headerTable.size(); i++) {
            TreeNode cur = headerTable.get(i).getNextHomonym();
            if (cur != null) prePageSet = new LinkedList<List<TreeNode>>();
            while (cur != null) {
                int counter = cur.getCount();
                List<TreeNode> prePage = new LinkedList<TreeNode>();
                TreeNode parent = cur;
                prePage.add(parent);
                while ((parent = parent.getParent()) != null
                        && !(parent.getName().equals("root"))) {
                    prePage.add(parent);
                }
                //按counter的数量作为添加次数
                while (counter-- > 0) {
                    prePageSet.add(prePage);
                }
                cur = cur.getNextHomonym();
            }
            List<List<String>> invPath = InvTree.buildInvTree(prePageSet, fstree.getMinSuport() * transRecords.size() / 100.0f);
            for (int j = 0; j < invPath.size(); j++) {
                //invStrList(trimSep(invPath.get(j))), mftss)) {
                //把支持度作为路径的一项添加进来，支持度的逆序路径的最后一个节点的count值
                invPath.get(j).add("sup=" + invStrList(invPath.get(j)).get(0).substring(invStrList(invPath.get(j)).get(0).indexOf(":") + 1) + " ");
                mftss.add(invStrList(trimSep(invPath.get(j))));
            }
        }
        //删除长度小于2的极大频繁序列
        removeLessThan(mftss);
        //把重复的子频繁序列删除
        removeDump(mftss);
    }

    /**
     * 这里可以设置极大频繁序列的最小长度，默认为2
     *
     * @param mftss2
     */
    private static void removeLessThan(List<List<String>> mftss2) {
        //删除长度小于最小长度（默认为2）的极大频繁序列
        for (int i = 0; i < mftss2.size(); i++) {
            if (mftss2.get(i).size() <= 2) {
                mftss2.remove(mftss2.get(i));
                i--;
            } else if (mftss2.get(i).size() == 3 && mftss2.get(i).get(2).equals("null")) {
                mftss2.remove(mftss2.get(i));
                i--;
            }
        }
    }

    /**
     * 删除极大频繁序列集中的非极大频繁序列以及只包含一个页面的频繁序列
     *
     * @param mftss2 极大频繁序列集
     */
    private static void removeDump(List<List<String>> mftss2) {

        //删除非极大的频繁序列
        for (int i = 0; i < mftss2.size(); i++) {
            String tmp = StringUtil.list2Str(mftss2.get(i)).substring(StringUtil.list2Str(mftss2.get(i)).indexOf(" ") + 1);
            //
            for (int j = 0; j < mftss2.size(); j++) {
                if (j == i) continue;
                if (StringUtil.KMP_Index(StringUtil.list2Str(mftss2.get(j)), tmp)) {
                    mftss2.remove(mftss2.get(i));
                    i--;
                    break;
                }
            }
        }
    }

    /**
     * 以极大频繁序列的长度降序排序
     *
     * @param lists 需要排序的list
     * @return 排序后的list
     */
    public static List<List<String>> sortByLengthDesc(List<List<String>> lists) {
        List<String> temp = new LinkedList<String>();
        for (int i = 0; i < lists.size(); i++) {
            lists.get(i).add("" + lists.get(i).size());
        }
        for (int i = 0; i < lists.size(); i++) {
            for (int j = i; j < lists.size(); j++) {
                if (Integer.parseInt(lists.get(i).get(lists.get(i).size() - 1)) < Integer.parseInt(lists.get(j).get(lists.get(j).size() - 1))) {
                    temp = lists.get(i);
                    lists.set(i, lists.get(j));
                    lists.set(j, temp);
                }
            }
        }
        for (int i = 0; i < lists.size(); i++) {
            lists.get(i).remove(lists.get(i).size() - 1);
        }
        return lists;
    }

    /**
     * 以极大频繁序列的长度升序排序
     *
     * @param lists 需要排序的list
     * @return 排序后的list
     */
    public static List<List<String>> sortByLengthAsc(List<List<String>> lists) {
        List<String> temp = new LinkedList<String>();
        for (int i = 0; i < lists.size(); i++) {
            lists.get(i).add("" + lists.get(i).size());
        }
        for (int i = 0; i < lists.size(); i++) {
            for (int j = i; j < lists.size(); j++) {
                if (Integer.parseInt(lists.get(i).get(lists.get(i).size() - 1)) > Integer.parseInt(lists.get(j).get(lists.get(j).size() - 1))) {
                    temp = lists.get(i);
                    lists.set(i, lists.get(j));
                    lists.set(j, temp);
                }
            }
        }
        for (int i = 0; i < lists.size(); i++) {
            lists.get(i).remove(lists.get(i).size() - 1);
        }
        return lists;
    }

    /**
     * 以极大频繁序列的长度升序排序
     *
     * @param lists 需要排序的list
     * @return 排序后的list
     */
    public static List<List<String>> sortBySupAsc(List<List<String>> lists) {
        List<String> temp = new LinkedList<String>();
        for (int i = 0; i < lists.size(); i++) {
            for (int j = i; j < lists.size(); j++) {
                int beginIndex = lists.get(i).get(0).indexOf("=") + 1;
                int endIndex = lists.get(i).get(0).indexOf(" ");
                //
                if (Integer.parseInt(lists.get(i).get(0).substring(beginIndex, endIndex)) > Integer.parseInt(lists.get(j).get(0).substring(beginIndex, endIndex))) {
                    temp = lists.get(i);
                    lists.set(i, lists.get(j));
                    lists.set(j, temp);
                }
            }
        }
        return lists;
    }

    /**
     * 以极大频繁序列的长度升序排序
     *
     * @param lists 需要排序的list
     * @return 排序后的list
     */
    public static List<List<String>> sortBySupDesc(List<List<String>> lists) {
        List<String> temp = new LinkedList<String>();
        for (int i = 0; i < lists.size(); i++) {
            for (int j = i; j < lists.size(); j++) {
                int beginIndex = lists.get(i).get(0).indexOf("=") + 1;
                int endIndex = lists.get(i).get(0).indexOf(" ");
                //
                if (Integer.parseInt(lists.get(i).get(0).substring(beginIndex, endIndex).trim()) < Integer.parseInt(lists.get(j).get(0).substring(beginIndex, endIndex).trim())) {
                    temp = lists.get(i);
                    lists.set(i, lists.get(j));
                    lists.set(j, temp);
                }
            }
        }
        return lists;
    }

    /**
     * 获取极大频繁序列挖掘结果的接口函数
     *
     * @return
     */
    public static JSONArray getMFSResults(List<List<String>> routes, String minSup) {
        JSONArray freqSequences = new JSONArray();
        init(routes, minSup);
        try {
            findPrePages(HeaderTable);
        } catch (NullPointerException e) {

        }

        mftss = sortBySupDesc(mftss);
        for (int i = 0; i < mftss.size(); i++) {
            //FreqSeqBean freqSeq= new FreqSeqBean();
            JSONObject freqSeq = new JSONObject();
            if (mftss.get(i).size() > 0) {
                //freqSeq.setId(i+1);
                //freqSeq.setFrequency(Integer.parseInt(mftss.get(i).get(0).substring(4, mftss.get(i).get(0).length() - 1)));
                freqSeq.put("id", i + 1);
                freqSeq.put("frequency", Integer.parseInt(mftss.get(i).get(0).substring(4, mftss.get(i).get(0).length() - 1)));
                String sequence = "";
                for (int j = 1; j < mftss.get(i).size() - 1; j++) {
                    sequence += mftss.get(i).get(j) + "->";
                }
                sequence += mftss.get(i).get(mftss.get(i).size() - 1);
                //freqSeq.setFreq_seq(sequence);
                freqSeq.put("freq_seq", sequence);
            }
//			freqSequences.add(freqSeq);
            freqSequences.add(freqSeq);
        }

        //用1模拟100
        //int size=(int)(mftss.size()*100-(10*Math.random()+1));
        //用50模拟100
        int size=(int)(mftss.size());

        System.out.println("size="+size);
        return freqSequences;
    }
}

