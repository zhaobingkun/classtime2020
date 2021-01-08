package com.bigdata.flow;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;

public class FlowPartition  extends Partitioner<Text,FlowBean> {
    @Override
    public int getPartition(Text text, FlowBean flowBean, int i) {
        String tel = text.toString();
        if (tel.startsWith("135")){
            return 0;
        }
        else if(tel.startsWith("136")){
            return 1;
        }
        else if(tel.startsWith("137")){
            return 2;
        }
        else{
            return 3;
        }
    }
}
