package com.bigdata.flow;

import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class FlowBean implements WritableComparable<FlowBean> {

    //1363157995033 	15920133257	5C-0E-8B-C7-BA-20:CMCC	120.197.40.4	sug.so.360.cn	信息安全	20	       20	        3156	 2936	   200
    //时间戳			手机号		基站编号				IP				URL				URL类型	   上行数据包  下行数据包   上行流量 下行流量  响应


    private String tel;
    private long downCount;
    private int upCount;
    private long sumCount;


    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public long getDownCount() {
        return downCount;
    }

    public void setDownCount(long downCount) {
        this.downCount = downCount;
    }

    public int getUpCount() {
        return upCount;
    }

    public void setUpCount(int upCount) {
        this.upCount = upCount;
    }

    public long getSumCount() {
        return sumCount;
    }

    public void setSumCount(long sumCount) {
        this.sumCount = sumCount;
    }

    @Override
    public int compareTo(FlowBean o) {
        int result = 0;
        result = this.upCount-o.getUpCount();
        if (result<0){
            result = 1;
        }
        else{
            result = -1;
        }
        return result;
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeUTF(tel);
        dataOutput.writeInt(upCount);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        this.tel = dataInput.readUTF();
        this.upCount = dataInput.readInt();
    }


    @Override
    public String toString() {
        return "FlowBean{" +
                "tel='" + tel + '\'' +
                ", downCount=" + downCount +
                ", upCount=" + upCount +
                ", sumCount=" + sumCount +
                '}';
    }
}
