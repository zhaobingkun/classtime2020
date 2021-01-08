package com.bigdata.flow;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class FlowSortMapper extends Mapper<LongWritable, Text,FlowBean,Text> {
    //1363157995033 	15920133257	5C-0E-8B-C7-BA-20:CMCC	120.197.40.4	sug.so.360.cn	信息安全	20	       20	        3156	 2936	   200
    //时间戳			手机号		基站编号				IP				URL				URL类型	   上行数据包  下行数据包   上行流量 下行流量  响应
// 13480253104	13480253104	41580	     41580	     2494800	41580
// 手机号        手机号      上行数据包  下行数据包   上行流量    下行流量

    @Override
    protected void map(LongWritable key, Text value, Context context)  {
        try {


            String[] split = value.toString().split("\t");

            FlowBean flowBean = new FlowBean();
            flowBean.setUpCount(Integer.parseInt(split[4]));
            String tel = split[1];
            flowBean.setTel(tel);
            context.write(flowBean, new Text(tel));
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }
}
