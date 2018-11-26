package com.example.boottest.demo.recommendation.ctx;

import com.example.boottest.demo.recommendation.model.ContextInfo;
import com.example.boottest.demo.recommendation.model.stats.BaseItem;
import com.example.boottest.demo.recommendation.model.stats.StatsContext;
import com.example.boottest.demo.utils.GsonUtil;
import com.example.boottest.demo.utils.mongodb.MongoDBUtil;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCursor;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Guan
 * @date Created on 2018/11/23
 */
@Repository
public class ContextInfoDao {

    private static final Logger logger = LoggerFactory.getLogger(ContextInfoDao.class);
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


    /**
     * 查找 contextInfo 表中的所有数据
     *
     * @return
     */
    public List<ContextInfo> findContextInfo() {
        FindIterable<Document> findIterable = MongoDBUtil.instance.getCollection(TN_PUSH_CONTEXT).find();
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
    public List<BaseItem> findContextInfoWithAggregate(String groupKey) {
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
        //$group
        BasicDBObject group = new BasicDBObject();
        group.put("_id", "$" + groupKey);
        group.put("count", new BasicDBObject("$sum", 1));
        //$project
        BasicDBObject project = new BasicDBObject("name", "$_id");
        project.put("count", 1);

        aggregateList.add(new BasicDBObject("$group", group));
        aggregateList.add(new BasicDBObject("$project", project));

        AggregateIterable<Document> findIterable = MongoDBUtil.instance.getCollection(TN_PUSH_CONTEXT).aggregate(aggregateList);
        MongoCursor<Document> mongoCursor = findIterable.iterator();
        List<BaseItem> list = new ArrayList<>();
        while (mongoCursor.hasNext()) {
            String json = mongoCursor.next().toJson();
            logger.info(json);
            list.add(GsonUtil.fromJson(json, BaseItem.class));
        }
        return list;
    }

    public static void main(String[] args) {
        ContextInfoDao contextInfoDao = new ContextInfoDao();
        contextInfoDao.findContextInfoWithAggregate("timeSegment");
    }

}
