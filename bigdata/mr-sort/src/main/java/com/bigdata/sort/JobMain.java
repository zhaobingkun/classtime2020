package com.bigdata.sort;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

import java.io.IOException;

public class JobMain {

    /**
     *

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
     */


    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        //配置文件
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf,"sort");
        //输入
        job.setInputFormatClass(TextInputFormat.class);
        TextInputFormat.addInputPath(job,new Path("d:/sort.txt"));
        //mapper
        job.setMapperClass(SortMapper.class);
        job.setMapOutputKeyClass(SortBean.class);
        job.setMapOutputValueClass(NullWritable.class);
        //reduce
        job.setReducerClass(SortReduce.class);
        job.setOutputKeyClass(SortBean.class);
        job.setOutputValueClass(NullWritable.class);
        //输出
        job.setOutputFormatClass(TextOutputFormat.class);
        TextOutputFormat.setOutputPath(job,new Path("d:/sort_out"));
        //等待运行
        boolean b = job.waitForCompletion(true);
        System.out.println(b);

        System.exit(b ? 0 : 1);



    }
}
