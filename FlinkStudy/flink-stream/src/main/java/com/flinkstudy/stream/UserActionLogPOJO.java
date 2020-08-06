package com.flinkstudy.stream;


import java.util.Objects;

public class UserActionLogPOJO {
    private String userID;
    private String productID;
    private int productPrice;

    public UserActionLogPOJO(String userID, String productID, int productPrice) {
        this.userID = userID;
        this.productID = productID;
        this.productPrice = productPrice;
    }

    @Override
    public String toString() {
        return "UserActionLogPOJO{" +
                "userID='" + userID + '\'' +
                ", productID='" + productID + '\'' +
                ", productPrice=" + productPrice +
                '}';
    }

    public UserActionLogPOJO(String userID) {
        this.userID = userID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(int productPrice) {
        this.productPrice = productPrice;
    }
}
