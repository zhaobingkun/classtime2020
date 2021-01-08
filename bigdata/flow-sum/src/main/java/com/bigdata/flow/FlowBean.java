package com.bigdata.flow;


import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class FlowBean implements  WritableComparable<FlowBean>{

    //1363157995033 	15920133257	5C-0E-8B-C7-BA-20:CMCC	120.197.40.4	sug.so.360.cn	信息安全	20	       20	        3156	 2936	   200
    //时间戳			手机号		基站编号				IP				URL				URL类型	   上行数据包  下行数据包   上行流量 下行流量  响应

    private  String tel;
    private long upData;
    private long downData;
    private long upCount;
    private long downCount;



    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public long getUpData() {
        return upData;
    }

    public void setUpData(long upData) {
        this.upData = upData;
    }

    public long getDownData() {
        return downData;
    }

    public void setDownData(long downData) {
        this.downData = downData;
    }

    public long getUpCount() {
        return upCount;
    }

    public void setUpCount(long upCount) {
        this.upCount = upCount;
    }

    public long getDownCount() {
        return downCount;
    }

    public void setDownCount(long downCount) {
        this.downCount = downCount;
    }


    @Override
    public int compareTo(FlowBean o) {
        return 0;
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeUTF(tel);
        dataOutput.writeLong(upData);
        dataOutput.writeLong(downData);
        dataOutput.writeLong(upCount);
        dataOutput.writeLong(downData);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        this.tel = dataInput.readUTF();
        this.upData = dataInput.readLong();
        this.downData = dataInput.readLong();
        this.upCount = dataInput.readLong();
        this.downCount = dataInput.readLong();
    }


    @Override
    public String toString() {
        return
               tel + '\t'+
               upData +'\t'+
               downData +'\t'+
               upCount +'\t'+
               downCount;
    }
}
