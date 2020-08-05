package com.classtime.springcloud.entities;

public class TitleInfo {
    private int rowNum;
    private int colNum;
    private String name;


    public int getRowNum() {
        return rowNum;
    }

    public void setRowNum(int rowNum) {
        this.rowNum = rowNum;
    }

    public int getColNum() {
        return colNum;
    }

    public void setColNum(int colNum) {
        this.colNum = colNum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "TitleInfo{" +
                "rowNum=" + rowNum +
                ", colNum=" + colNum +
                ", name='" + name + '\'' +
                '}';
    }
}
