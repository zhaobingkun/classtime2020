package com.bigdata.orders;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.OutputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

import java.io.IOException;

public class JobMain {




     static {
     try {
     // 设置 HADOOP_HOME 目录
     System.setProperty("hadoop.home.dir", "E:/download/dllwinutils");
     // 加载库文件
     System.load("E:/download/dllwinutils/hadoop.dll");
     } catch (UnsatisfiedLinkError e) {
     System.err.println("Native code library failed to load.\n" + e);
     System.exit(1);
     }
     }


    public static void main(String[] args) {
         try {
             //1、配置
             Configuration conf = new Configuration();
             Job job = Job.getInstance(conf, "OrderGroup");
             //2、输入
             job.setInputFormatClass(TextInputFormat.class);
             TextInputFormat.addInputPath(job, new Path("D:/bigdatatest/orders.txt"));

             //3、map及参数
             job.setMapperClass(OrderMapper.class);
             job.setMapOutputKeyClass(OrderBean.class);
             job.setMapOutputValueClass(Text.class);
             //4、shuffer 分组，pattition
             job.setPartitionerClass(OrderPartition.class);
             job.setGroupingComparatorClass(OrderGroup.class);
             //job.setNumReduceTasks(1);
             //5、reduce及参数
             job.setReducerClass(OrderReduce.class);
             job.setOutputKeyClass(Text.class);
             job.setOutputValueClass(NullWritable.class);
             //6、输出
             job.setOutputFormatClass(TextOutputFormat.class);

             TextOutputFormat.setOutputPath(job, new Path("d:/bigdatatest/order_group_out"));
             //7、等待结束
             boolean b = job.waitForCompletion(true);
             System.out.println(b);
             System.exit(b ? 0 : 1);
             //8
         }catch (Exception e){
             e.printStackTrace();
         }
    }
}
