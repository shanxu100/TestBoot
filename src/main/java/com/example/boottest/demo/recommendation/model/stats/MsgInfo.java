package com.example.boottest.demo.recommendation.model.stats;

/**
 * 对应消息列表中的一条消息实体类
 * 这条消息是通过师兄的接口返回的数据，是经过统计后的数据，不是直接从数据库中读的数据
 *
 * @author Guan
 * @date Created on 2018/12/5
 */
public class MsgInfo {
    public String appId;
    public String messageId;

    public String title;
    public String content;
    public String time;

    public int totalCount;
    public int pushCount;
    public int cancelCount;
    public int openCount;


}
