package com.example.boottest.demo.recommendation.rcmd.manager;

import com.example.boottest.demo.utils.LogUtils;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.similarity.precompute.example.GroupLensDataModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

/**
 * @author Guan
 * @date Created on 2018/11/9
 */
@Component
public class GroupLensRcmdManager extends AbstractRcmdManager {
    private static final Logger logger = LoggerFactory.getLogger(GroupLensRcmdManager.class);

    @Override
    @Async
    public DataModel loadDataModel() {
        try {
            logger.info("开始加载DataModel...");
            //记录开始时间，计算耗时
            long startTime = System.currentTimeMillis();
            //准备数据 这里是电影评分数据
            File file = new File(datasetPath);
            //将数据加载到内存中，GroupLensDataModel是针对开放电影评论数据的
            DataModel dataModel = new GroupLensDataModel(file);

            LogUtils.printRunningTime("加载DataModel", startTime);
            return dataModel;

        } catch (IOException e) {
            e.printStackTrace();
            logger.error(logger.getName(), e);
            return null;
        }

    }


}
