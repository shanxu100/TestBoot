package com.example.boottest.demo.recommendation;

import com.example.boottest.demo.recommendation.model.ContextInfo;
import com.example.boottest.demo.utils.mongodb.MongoDBUtil;
import org.bson.Document;
import org.springframework.stereotype.Repository;

/**
 * @author Guan
 * @date Created on 2018/11/23
 */
@Repository
public class ContextInfoDao {

    private static final String TN_PUSH_CONTEXT = "PushContext";

    /**
     * 插入情景信息
     *
     * @param contextInfo
     */
    public void addContextInfo(ContextInfo contextInfo) {
        Document document = Document.parse(contextInfo.toJson());
        MongoDBUtil.instance.getCollection(TN_PUSH_CONTEXT).insertOne(document);

    }

}
