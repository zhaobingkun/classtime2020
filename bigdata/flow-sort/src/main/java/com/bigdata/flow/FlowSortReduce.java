package com.bigdata.flow;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class FlowSortReduce extends Reducer<FlowBean,Text,Text,FlowBean> {
    @Override
    protected void reduce(FlowBean key, Iterable<Text> values, Context context){

        //System.out.println(values.iterator().next());
        //System.out.println("key-upcount="+key.getUpCount());
        try {
            context.write(values.iterator().next(), key);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
