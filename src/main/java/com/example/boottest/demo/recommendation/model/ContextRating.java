package com.example.boottest.demo.recommendation.model;

/**
 * 用于记录 User-Item-Rating-Context 的四维信息
 *
 * @author Guan
 * @date Created on 2018/12/5
 */
public class ContextRating extends BaseInfo {

    public String deviceId;
    public String messageId;

    /**
     * 区别于MessageId，用于推荐。
     * 本质是MessageId的HashCode
     */
    private long itemId = -1;
    private long userId = -1;

    public int rating;

    public String contextId;


    public long getItemId() {
        if (this.itemId == -1) {
            initItemId();
        }
        return itemId;
    }

    /**
     * 初始化itemId
     */
    public void initItemId() {
        if (messageId != null) {
            this.itemId = messageId.hashCode() & 0x7FFFFFFF;
        }
    }

    /**
     * 初始化UserId
     * 因为HashCode可能为负数，所以通过位运算解决一下
     */
    public void initUserId() {
        if (deviceId != null) {
            this.userId = deviceId.hashCode() & 0x7FFFFFFF;
        }
    }


    public static ContextRating newInstance(ContextInfo contextInfo) {
        ContextRating contextRating = new ContextRating();

        contextRating.deviceId = contextInfo.deviceId;
        contextRating.messageId = contextInfo.messageId;

        contextRating.initItemId();
        contextRating.initUserId();

        contextRating.rating = contextInfo.rating;
        contextRating.contextId = contextInfo.getContextId();
        return contextRating;
    }

}
