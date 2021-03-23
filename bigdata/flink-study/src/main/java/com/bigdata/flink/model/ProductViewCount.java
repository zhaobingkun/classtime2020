package com.bigdata.flink.model;

import java.io.Serializable;

public class ProductViewCount implements Serializable{//,Comparable<ProductViewCount> {
    private Long productId;
    private Long windowEnd;
    private Long count;

    public ProductViewCount(Long productId, Long windowEnd, Long count) {
        this.productId = productId;
        this.windowEnd = windowEnd;
        this.count = count;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getWindowEnd() {
        return windowEnd;
    }

    public void setWindowEnd(Long windowEnd) {
        this.windowEnd = windowEnd;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "ProductViewCount{" +
                "productId=" + productId +
                ", windowEnd=" + windowEnd +
                ", count=" + count +
                '}';
    }

 /*   @Override
    public int compareTo(ProductViewCount o) {
        return o.getCount().compareTo(this.getCount());
    }*/

}
