package com.bigdata.flow;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class FlowPartitionReduce extends Reducer<Text, FlowBean, Text, FlowBean>  {
    @Override
    protected void reduce(Text key, Iterable<FlowBean> values, Context context) throws IOException, InterruptedException {
        //1363157995033 	15920133257	5C-0E-8B-C7-BA-20:CMCC	120.197.40.4	sug.so.360.cn	信息安全	20	       20	        3156	 2936	   200
        //时间戳			手机号		基站编号				IP				URL				URL类型	   上行数据包  下行数据包   上行流量 下行流量  响应
try {
    long upCount = 0;
    long downCount = 0;
    long upData = 0;
    long downData = 0;

    //汇总
    for (FlowBean value : values) {
        upData += value.getUpData();
        downData += value.getDownData();
        upCount += value.getUpCount();
        downCount += value.getDownCount();
    }

    FlowBean flowBean = new FlowBean();
    flowBean.setDownCount(downCount);
    flowBean.setUpCount(upCount);

    flowBean.setDownData(downData);
    flowBean.setUpData(upData);

    System.out.println(key.toString());
    flowBean.setTel(key.toString());

    //输出
    context.write(key, flowBean);
}
catch (Exception e){
    e.printStackTrace();
}


    }
}
