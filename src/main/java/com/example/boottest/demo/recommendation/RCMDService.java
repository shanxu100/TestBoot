package com.example.boottest.demo.recommendation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Guan
 * @date Created on 2018/11/7
 */
@Service
public class RCMDService {

    @Autowired
    UBRCMD ubr;

    public void userBasedRecommend(long userId, int howMany){

        try {
            ubr.recommend(userId,howMany);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
