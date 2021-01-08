package com.bigdata.flow;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
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

    public static void main(String[] args)  {
        try {
            //1、配置
            Configuration conf = new Configuration();
            Job job = Job.getInstance(conf, "Partition");
            job.setJarByClass(JobMain.class);
            //2、输入
            job.setInputFormatClass(TextInputFormat.class);
            TextInputFormat.addInputPath(job, new Path("D://bigdatatest//flow//flow.log"));

            //3、patition
            job.setPartitionerClass(FlowPartition.class);
            //4、mapper 及参数
            job.setMapperClass(FlowPartitionMapper.class);
            job.setMapOutputKeyClass(Text.class);
            job.setMapOutputValueClass(FlowBean.class);
             //5、reduce 及参数
            job.setReducerClass(FlowPartitionReduce.class);
            job.setOutputKeyClass(Text.class);
            job.setOutputValueClass(FlowBean.class);

            //4、输出
            job.setOutputFormatClass(TextOutputFormat.class);
            TextOutputFormat.setOutputPath(job, new Path("D://bigdatatest/flow/flow_partition_out"));

            //7、设置task数量
            job.setNumReduceTasks(4);
            //8、等待完成

            boolean b = job.waitForCompletion(true);
            System.out.println(b);
            System.exit(b ? 0 : 1);
        }
        catch(Exception e){
            e.printStackTrace();

        }



    }
}
