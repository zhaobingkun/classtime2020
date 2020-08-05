package com.classtime.springcloud.entities;

public class Work {
    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    private  String bankName;
    private String sourceName;
    private String sourceCode;
    private float score;
    private String bankNameSubStr;
    private String sourceNameSbuStr;


    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getSourceCode() {
        return sourceCode;
    }

    public void setSourceCode(String sourceCode) {
        this.sourceCode = sourceCode;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public String getBankNameSubStr() {
        return bankNameSubStr;
    }

    public void setBankNameSubStr(String bankNameSubStr) {
        this.bankNameSubStr = bankNameSubStr;
    }

    public String getSourceNameSbuStr() {
        return sourceNameSbuStr;
    }

    public void setSourceNameSbuStr(String sourceNameSbuStr) {
        this.sourceNameSbuStr = sourceNameSbuStr;
    }
}
