package com.bigdata.flow;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class FlowSumReduce extends Reducer<Text, FlowBean, Text, FlowBean> {
    @Override
    protected void reduce(Text key, Iterable<FlowBean> values, Context context) throws IOException, InterruptedException {
        long upData = 0;
        long downData = 0;
        long upCount = 0;
        long downCount = 0;
        for  (FlowBean value:values){
            upData += value.getUpData();
            downData += value.getDownData();
            upCount += value.getUpCount();
            downCount += value.getDownCount();

         }
        FlowBean flowBean = new FlowBean();
        flowBean.setUpData(upData);
        flowBean.setDownData(downData);
        flowBean.setUpCount(upCount);
        flowBean.setDownCount(downCount);
        flowBean.setTel(key.toString());
        context.write(key,flowBean);
    }
}
