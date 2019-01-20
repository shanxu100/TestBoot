package com.example.boottest.demo.recommendation.seq.gsp.gsp2;

import java.util.ArrayList;

/**
 * @author Guan
 * @date Created on 2019/1/12
 */
public class gsp2 {
    public static void main(String[] args) {
        testGSP();
    }

    public static void testGSP() {
        GSP gsp = new GSP(2);


        gsp.outputInput();
        ArrayList<Sequence> result = gsp.getSequences();

        for (Sequence sequence:result){
            for (int i=0;i<sequence.size();i++){
                System.out.print(sequence.getElement(i));
            }
            System.out.println();
        }

    }

}
