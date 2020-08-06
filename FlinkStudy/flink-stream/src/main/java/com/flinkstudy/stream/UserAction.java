package com.flinkstudy.stream;

public class UserAction {
    private  String userID;     //=userID1,
    private  int eventTime;     //=1293984002,
    private  String eventType;  //=click,
    private  String productID;   //=productID1, p
    private  int   ProductPrice;  //=80

    public UserAction(String userID, int eventTime, String eventType, String productID, int ProductPrice) {
        this.userID = userID;
        this.eventTime = eventTime;
        this.eventType = eventType;
        this.productID = productID;
        this.ProductPrice = ProductPrice;
    }

    @Override
    public String toString() {
        return "UserAction{" +
                "userID='" + userID + '\'' +
                ", eventTime=" + eventTime +
                ", eventType='" + eventType + '\'' +
                ", productID='" + productID + '\'' +
                ", ProductPrice=" + ProductPrice +
                '}';
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public int getEventTime() {
        return eventTime;
    }

    public void setEventTime(int eventTime) {
        this.eventTime = eventTime;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public int getProductPrice() {
        return ProductPrice;
    }

    public void setProductPrice(int productPrice) {
        this.ProductPrice = productPrice;
    }
}
