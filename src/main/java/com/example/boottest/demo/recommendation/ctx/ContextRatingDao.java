package com.example.boottest.demo.recommendation.ctx;

import com.example.boottest.demo.recommendation.model.ContextRating;
import com.example.boottest.demo.utils.mongodb.origin.MongoDBUtil;
import com.google.gson.Gson;
import com.mongodb.client.FindIterable;
import org.bson.Document;
import org.bson.json.Converter;
import org.bson.json.JsonWriterSettings;
import org.bson.json.StrictJsonWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Guan
 * @date Created on 2018/12/5
 */
@Repository
public class ContextRatingDao {

    private static final Logger logger = LoggerFactory.getLogger(ContextRatingDao.class);
    private static final String TN_PUSH_RATING = "PushRating";

    @Autowired
    MongoTemplate mongoTemplate;

    /**
     * 将数据库中 NumberLong 类型的值转换成java可识别的long类型
     */
    private static final JsonWriterSettings jsonWriterSettings = JsonWriterSettings.builder().int64Converter(new Converter<Long>() {
        @Override
        public void convert(Long value, StrictJsonWriter writer) {
            writer.writeNumber(value + "");
        }
    }).build();


    public void addContextRating(ContextRating contextRating) {
        Document document = Document.parse(contextRating.toJson());
//        MongoDBUtil.instance.getCollection(TN_PUSH_CONTEXT).insertOne(document);
        mongoTemplate.getCollection(TN_PUSH_RATING).insertOne(document);

    }

    /**
     * 获取数据库的评分数据
     *
     * @return
     */
    public List<String> getContextRating() {
        FindIterable<Document> findIterable = mongoTemplate.getCollection(TN_PUSH_RATING).find();
        Iterator<Document> iterator = findIterable.iterator();
        Gson gson = new Gson();
        List<String> list = new ArrayList<>();
        while (iterator.hasNext()) {
            String json = iterator.next().toJson();
            ContextRating contextRating = gson.fromJson(json, ContextRating.class);
            list.add(contextRating.toFormattedString());
        }
        logger.info("从 {} 表中获取评分记录数据，共 {} 条", TN_PUSH_RATING, list.size());
        return list;

    }

}
