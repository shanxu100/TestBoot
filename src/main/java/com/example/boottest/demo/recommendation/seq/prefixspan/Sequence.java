package com.example.boottest.demo.recommendation.seq.prefixspan;



import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author xiangli chen
 */
public class Sequence {
    public ArrayList<ArrayList<Integer>> sequence;

    public double mis;

    public int frequence;

    public int maxSup;

    public int minSup;

    public Sequence() {
        this.sequence = new ArrayList<ArrayList<Integer>>();
        this.mis = 0;
        this.frequence = 0;
        this.maxSup = 0;
        this.minSup = 0;
    }

    public Sequence(Item item) {
        this.sequence = new ArrayList<ArrayList<Integer>>();
        ArrayList<Integer> subSequence = new ArrayList<Integer>();
        subSequence.add(item.key);
        this.sequence.add(subSequence);
        this.mis = item.mis;
        this.frequence = item.frequence;
        this.maxSup = item.frequence;
        this.minSup = item.frequence;
    }

    public Sequence(ArrayList<ArrayList<Integer>> sequence) {
        this.sequence = new ArrayList<ArrayList<Integer>>();
        ArrayList<Integer> subSequence = null;
        Iterator<ArrayList<Integer>> iteALI = null;
        Iterator<Integer> iteI = null;
        iteALI = sequence.iterator();
        while (iteALI.hasNext()) {
            subSequence = new ArrayList<Integer>();
            iteI = iteALI.next().iterator();
            while (iteI.hasNext()) {
                subSequence.add(iteI.next());
            }
            this.sequence.add(subSequence);
        }
        this.mis = 0;
        this.frequence = 0;
        this.maxSup = 0;
        this.minSup = 0;
    }

    public Sequence(Sequence s) {
        this.sequence = new Sequence(s.sequence).sequence;
        this.mis = s.mis;
        this.frequence = s.frequence;
        this.maxSup = s.maxSup;
        this.minSup = s.minSup;
    }

    public Sequence copySequence() {
        Sequence s = new Sequence();
        ArrayList<Integer> subSequence = null;
        Iterator<ArrayList<Integer>> iteALI = null;
        Iterator<Integer> iteI = null;
        iteALI = this.sequence.iterator();
        while (iteALI.hasNext()) {
            subSequence = new ArrayList<Integer>();
            iteI = iteALI.next().iterator();
            while (iteI.hasNext()) {
                subSequence.add(iteI.next());
            }
            s.sequence.add(subSequence);
        }
        s.mis = this.mis;
        s.frequence = this.frequence;
        s.maxSup = this.maxSup;
        s.minSup = this.minSup;
        return s;
    }

    public boolean contain_item(int item) {
        Iterator<ArrayList<Integer>> ite1 = sequence.iterator();
        Iterator<Integer> ite2 = null;
        while (ite1.hasNext()) {
            ite2 = ite1.next().iterator();
            while (ite2.hasNext()) {
                if (ite2.next() == item)
                    return true;
            }
        }
        return false;
    }

    // @SuppressWarnings("unchecked")
    public ArrayList<ArrayList<Integer>> sequenceProject(
            ArrayList<Integer> prefix) {
        // TODO Auto-generated method stub
        ArrayList<ArrayList<Integer>> sequence = null;
        ArrayList<Integer> temSubSequence = null;
        ArrayList<Integer> temItems = null;
        Iterator<ArrayList<Integer>> iteALI = this.sequence.iterator();
        sequence = new Sequence(this.sequence).sequence;
        iteALI = sequence.iterator();
        boolean seperate = (prefix.size() == 1);
        temItems = iteALI.next();
        if (temItems.get(0) == 0) {// {_,70,80}
            if (!seperate) {// {30,40}
                temItems.remove(0);
                ArrayList<Integer> temPrefix = new ArrayList<Integer>();
                temPrefix.add(prefix.get(prefix.size() - 1));
                temSubSequence = subSequenceProject(temItems, temPrefix);
                iteALI.remove();
                if (temSubSequence != null) {
                    if (temSubSequence.isEmpty()) {
                        return sequence;
                    } else {
                        sequence.add(0, temSubSequence);
                        return sequence;
                    }
                }
            } else {
                iteALI.remove();// {30}{x}
            }
        } else {
            iteALI = sequence.iterator();
        }
        while (iteALI.hasNext()) {
            temItems = iteALI.next();
            temSubSequence = subSequenceProject(temItems, prefix);
            iteALI.remove();
            if (temSubSequence != null) {
                if (temSubSequence.isEmpty()) {
                    return sequence;
                } else {
                    sequence.add(0, temSubSequence);
                    return sequence;
                }
            }
        }
        return null;
    }

    public static ArrayList<Integer> subSequenceProject(
            ArrayList<Integer> subSequence, ArrayList<Integer> prefix) {
        if (subSequence.size() < prefix.size())
            return null;
        ArrayList<Integer> temSubSequence = new ArrayList<Integer>();
        Iterator<Integer> iteI = subSequence.iterator();
        while (iteI.hasNext()) {
            temSubSequence.add(iteI.next());
        }
        for (int i = 0; i < prefix.size(); ) {
            while (!temSubSequence.isEmpty()) {
                if (prefix.get(i).intValue() == temSubSequence.get(0).intValue()) {
                    i++;
                    temSubSequence.remove(0);
                    break;
                } else {
                    temSubSequence.remove(0);
                }
            }
            if (i < prefix.size() && temSubSequence.isEmpty()) {
                return null;
            }
        }
        if (!temSubSequence.isEmpty()) {
            temSubSequence.add(0, 0);
        }
        return temSubSequence;
    }


}//