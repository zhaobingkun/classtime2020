package com.bigdata.flink.model;

import java.io.Serializable;

public class UserBehavior implements Serializable {

    private  Long userId;
    private  Long productId;
    private  Long categoryId;
    private  String behavior;
    private  Long timeStamp;
    private  String sessionId;

    public UserBehavior(Long userId, Long productId, Long categoryId, String behavior, Long timeStamp, String sessionId) {
        this.userId = userId;
        this.productId = productId;
        this.categoryId = categoryId;
        this.behavior = behavior;
        this.timeStamp = timeStamp;
        this.sessionId = sessionId;
    }

    @Override
    public String toString() {
        return "UserBehavior{" +
                "userId=" + userId +
                ", productId=" + productId +
                ", categoryId=" + categoryId +
                ", behavior='" + behavior + '\'' +
                ", timeStamp=" + timeStamp +
                ", sessionId='" + sessionId + '\'' +
                '}';
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getBehavior() {
        return behavior;
    }

    public void setBehavior(String behavior) {
        this.behavior = behavior;
    }

    public Long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}
