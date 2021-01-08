package com.bigdata.orders;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class OrderReduce extends Reducer<OrderBean, Text,Text, NullWritable> {
    @Override
    protected void reduce(OrderBean key, Iterable<Text> values, Context context){
        try {
            int i = 0;
            System.out.println(key.toString());
            for (Text value : values) {
                System.out.println(value);
                context.write(value, NullWritable.get());
                i++;
                if (i >= 1) {
                    break;
                }

            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
