package com.bigdata.orders;


import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;

import org.apache.hadoop.mapreduce.Mapper;


import java.io.IOException;

public class OrderMapper extends Mapper<LongWritable, Text,OrderBean,Text> {

    @Override
    protected void map(LongWritable key, Text value, Context context)  {
        try {
            String[] split = value.toString().split("\t");
            OrderBean orderBean = new OrderBean();
            orderBean.setOrderId(split[0]);
            orderBean.setGoods(split[1]);
            orderBean.setPrice(Double.parseDouble(split[2]));
            System.out.println(value);
            context.write(orderBean, value);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
