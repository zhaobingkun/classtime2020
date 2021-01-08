package com.bigdata.sort;

import org.apache.commons.lang.ObjectUtils;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class SortMapper extends Mapper<LongWritable, Text, SortBean, NullWritable> {
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
       String[] word = value.toString().split(" ");
       SortBean sortBean  = new SortBean();
       sortBean.setName(word[0]);
       sortBean.setNum(Integer.parseInt(word[1]));
       context.write(sortBean, NullWritable.get());

    }
}
