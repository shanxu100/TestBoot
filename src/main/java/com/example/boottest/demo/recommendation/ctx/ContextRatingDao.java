package com.example.boottest.demo.recommendation.ctx;

import com.example.boottest.demo.recommendation.model.ContextRating;
import com.example.boottest.demo.utils.mongodb.origin.MongoDBUtil;
import org.bson.Document;
import org.bson.json.Converter;
import org.bson.json.JsonWriterSettings;
import org.bson.json.StrictJsonWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

/**
 * @author Guan
 * @date Created on 2018/12/5
 */
@Repository
public class ContextRatingDao {

    private static final Logger logger = LoggerFactory.getLogger(ContextRatingDao.class);
    private static final String TN_PUSH_CONTEXT = "PushRating";

    /**
     * 将数据库中 NumberLong 类型的值转换成java可识别的long类型
     */
    private static final JsonWriterSettings jsonWriterSettings = JsonWriterSettings.builder().int64Converter(new Converter<Long>() {
        @Override
        public void convert(Long value, StrictJsonWriter writer) {
            writer.writeNumber(value + "");
        }
    }).build();


    public void addContextRating(ContextRating contextRating){
        Document document = Document.parse(contextRating.toJson());
        MongoDBUtil.instance.getCollection(TN_PUSH_CONTEXT).insertOne(document);
    }

}
