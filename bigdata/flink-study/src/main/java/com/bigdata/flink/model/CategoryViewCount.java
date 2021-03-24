package com.bigdata.flink.model;

import java.io.Serializable;

public class CategoryViewCount implements Serializable {
    private Long categoryId;
    private Long windowEnd;
    private Long count;

    public CategoryViewCount(Long categoryId, Long windowEnd, Long count) {
        this.categoryId = categoryId;
        this.windowEnd = windowEnd;
        this.count = count;
    }

    @Override
    public String toString() {
        return "CategoryViewCount{" +
                "categoryId=" + categoryId +
                ", windowEnd=" + windowEnd +
                ", count=" + count +
                '}';
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
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
}
