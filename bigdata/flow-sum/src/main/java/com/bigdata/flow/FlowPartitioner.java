package com.bigdata.flow;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;

public class FlowPartitioner extends Partitioner<Text, FlowBean> {
    @Override
    public int getPartition(Text text, FlowBean flowBean, int i) {
        String tel = text.toString();
        if (tel.startsWith("13")){
            return 0;
        }
        else if(tel.startsWith("15")){
            return 1;
        }
        else if(tel.startsWith("18")){
            return 2;
        }
        else{
            return 3;
        }



    }
}
