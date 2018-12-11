package com.example.boottest.demo.recommendation.ctx;

import com.example.boottest.demo.recommendation.model.ContextInfo;
import com.example.boottest.demo.recommendation.model.stats.BaseItem;
import com.example.boottest.demo.utils.GsonUtil;
import com.mongodb.BasicDBObject;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCursor;
import org.bson.Document;
import org.bson.conversions.Bson;
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
 * @date Created on 2018/11/23
 */
@Repository
public class ContextInfoDao {

    private static final Logger logger = LoggerFactory.getLogger(ContextInfoDao.class);
    private static final String TN_PUSH_CONTEXT = "PushContext";

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


    /**
     * 插入情景信息
     *
     * @param contextInfo
     */
    public void addContextInfo(ContextInfo contextInfo) {
        Document document = Document.parse(contextInfo.toJson());
//        MongoDBUtil.instance.getCollection(TN_PUSH_CONTEXT).insertOne(document);
        mongoTemplate.getCollection(TN_PUSH_CONTEXT).insertOne(document);
    }


    /**
     * 查找 contextInfo 表中的所有数据
     *
     * @return
     */
    public List<ContextInfo> findContextInfo() {

//        FindIterable<Document> findIterable = MongoDBUtil.instance.getCollection(TN_PUSH_CONTEXT).find();
        FindIterable<Document> findIterable = mongoTemplate.getCollection(TN_PUSH_CONTEXT).find();
        MongoCursor<Document> mongoCursor = findIterable.iterator();
        List<ContextInfo> list = new ArrayList<>();
        while (mongoCursor.hasNext()) {
            list.add(GsonUtil.fromJson(mongoCursor.next().toJson(), ContextInfo.class));
        }
        return list;
    }

    /**
     * 通过聚合操作，查找 contextInfo 表中指定字段的数量
     *
     * @return
     */
    public List<BaseItem> findContextInfoWithAggregate(String appId, String groupKey) {
        /*
        MongoDB的查询语句
        db.PushContext.aggregate(
        [
        {$group:{_id:"$timeSegment",count:{$sum:1}}},

        {$project: {newname:"$_id"}}
        ]
        )
         */
        List<Bson> aggregateList = new ArrayList<>();

        //$match
        BasicDBObject match = new BasicDBObject();
        match.put("appId", appId);
        //$group
        BasicDBObject group = new BasicDBObject();
        group.put("_id", "$" + groupKey);
        group.put("count", new BasicDBObject("$sum", 1));
        //$project
        BasicDBObject project = new BasicDBObject("x", "$_id");
        project.put("y", "$count");
        //$sort
        BasicDBObject sort = new BasicDBObject("x", 1);

        aggregateList.add(new BasicDBObject("$match", match));
        aggregateList.add(new BasicDBObject("$group", group));
        aggregateList.add(new BasicDBObject("$project", project));
        aggregateList.add(new BasicDBObject("$sort", sort));


//        AggregateIterable<Document> findIterable = MongoDBUtil.instance.getCollection(TN_PUSH_CONTEXT).aggregate(aggregateList);
        AggregateIterable<Document> findIterable = mongoTemplate.getCollection(TN_PUSH_CONTEXT).aggregate(aggregateList);

        MongoCursor<Document> mongoCursor = findIterable.iterator();
        List<BaseItem> list = new ArrayList<>();
        while (mongoCursor.hasNext()) {
            String json = mongoCursor.next().toJson();
            list.add(GsonUtil.fromJson(json, BaseItem.class));
        }
        return list;
    }


    /**
     * 查找每一个消息的平均阅读时间
     *
     * @param appId
     * @return
     */
    public List<BaseItem> findMsgReadingDurationWithAggregate(String appId) {
        List<Bson> aggregateList = new ArrayList<>();

        /*
        db.PushContext.aggregate(
        [
        {$group:{ _id: "$itemId", avg: { $avg: "$duration"} }},
        {$project: (
            {"x":"$_id","y":"$avg"}
        )}
        ]
        )
         */

        //$match
        BasicDBObject match = new BasicDBObject();
        match.put("appId", appId);
        //$group
        BasicDBObject group = new BasicDBObject();
        group.put("_id", "$messageId");
        group.put("avgTime", new BasicDBObject("$avg", "$duration"));
        //$project
        BasicDBObject project = new BasicDBObject("x", "$_id");
        project.put("y", "$avgTime");
        //$sort
        BasicDBObject sort = new BasicDBObject("y", -1);


        aggregateList.add(new BasicDBObject("$match", match));
        aggregateList.add(new BasicDBObject("$group", group));
        aggregateList.add(new BasicDBObject("$project", project));
        aggregateList.add(new BasicDBObject("$sort", sort));
        //排序后取前20个数据
        aggregateList.add(new BasicDBObject("$limit", 20));


//        AggregateIterable<Document> findIterable = MongoDBUtil.instance.getCollection(TN_PUSH_CONTEXT).aggregate(aggregateList);
        AggregateIterable<Document> findIterable = mongoTemplate.getCollection(TN_PUSH_CONTEXT).aggregate(aggregateList);
        MongoCursor<Document> mongoCursor = findIterable.iterator();
        List<BaseItem> list = new ArrayList<>();
        while (mongoCursor.hasNext()) {
            String json = mongoCursor.next().toJson();
            list.add(GsonUtil.fromJson(json, BaseItem.class));
        }
        return list;
    }

    public static void main(String[] args) {
        List<BaseItem> list = new ArrayList<>();
        list.add(new BaseItem("1", 2));
        list.add(new BaseItem("3", 4));
        list.add(new BaseItem("5", 6));

        Iterator<BaseItem> iterator = list.iterator();

        System.out.println(list.size());

    }

}
