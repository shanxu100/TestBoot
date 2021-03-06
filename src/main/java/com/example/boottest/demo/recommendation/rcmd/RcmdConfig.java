package com.example.boottest.demo.recommendation.rcmd;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @author Guan
 * @date Created on 2018/11/6
 */
@Configuration
@Component
public class RcmdConfig {

    @Value("${recommendation.NN.number}")
    private int NN;

    @Value("${recommendation.dataset.path}")
    private String datasetPath;


    @Bean(name = "datasetPath")
    public String getDatasetPath() {
        return datasetPath;
    }

    @Bean(name = "NN")
    public int getNearestNeighborhoodCount() {
        return NN;
    }

}
