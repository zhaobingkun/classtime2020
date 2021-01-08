package com.bigdata.sort;

import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class SortBean implements WritableComparable<SortBean> {
    private String name;
    private int num;

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public String toString() {
        return "sortBean{" +
                "name='" + name + '\'' +
                ", num=" + num +
                '}';
    }

    @Override
    public int compareTo(SortBean o) {
        int result = this.name.compareTo(o.getName());
        if (result == 0) {
            return   this.num - o.getNum();
        }
        return result;
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeUTF(name);
        dataOutput.writeInt(num);

    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        this.name=dataInput.readUTF();
        this.num=dataInput.readInt();
    }


}
