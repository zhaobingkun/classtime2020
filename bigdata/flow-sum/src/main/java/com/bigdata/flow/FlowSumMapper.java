package com.bigdata.flow;


import com.sun.corba.se.spi.ior.Writeable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class FlowSumMapper extends Mapper<LongWritable, Text, Text, FlowBean> {

    //1363157995033 	15920133257	5C-0E-8B-C7-BA-20:CMCC	120.197.40.4	sug.so.360.cn	信息安全	20	       20	        3156	 2936	   200
    //时间戳			手机号		基站编号				IP				URL				URL类型	   上行数据包  下行数据包   上行流量 下行流量  响应

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String[] words =value.toString().split("\t");
        String tel = words[1];
        FlowBean flowBean = new FlowBean();
        flowBean.setTel(words[1]);
        flowBean.setUpData(Long.parseLong(words[6]));
        flowBean.setDownData(Long.parseLong(words[7]));
        flowBean.setUpCount(Long.parseLong(words[8]));
        flowBean.setDownCount(Long.parseLong(words[9]));

        context.write(new Text(tel),flowBean);

    }
}
