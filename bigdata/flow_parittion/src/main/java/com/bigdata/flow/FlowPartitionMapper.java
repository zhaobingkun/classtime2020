package com.bigdata.flow;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class FlowPartitionMapper extends Mapper<LongWritable, Text, Text, FlowBean> {

    //1363157995033 	15920133257	5C-0E-8B-C7-BA-20:CMCC	120.197.40.4	sug.so.360.cn	信息安全	20	       20	        3156	 2936	   200
    //时间戳			手机号		基站编号				IP				URL				URL类型	   上行数据包  下行数据包   上行流量 下行流量  响应


    @Override
    protected void map(LongWritable key, Text value, Context context){
        try {
            //拆分
            String[] split = value.toString().split("\t");
            String tel = split[1];
            FlowBean flowBean = new FlowBean();
            flowBean.setTel(tel);

            flowBean.setUpData(Long.parseLong(split[6]));
            flowBean.setDownData(Long.parseLong(split[7]));
            flowBean.setUpCount(Long.parseLong(split[8]));
            flowBean.setDownCount(Long.parseLong(split[9]));
            //输出到reduce
            context.write(new Text(tel), flowBean);
        }
        catch(Exception e){
            e.printStackTrace();
        }

    }
}
