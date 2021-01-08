package com.bigdata.flow;

import org.apache.hadoop.conf.Configuration;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;;import java.io.IOException;

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
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        //1、配置
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf,"FlowSort");
        //2、输入,用汇总输出作为输入
        job.setInputFormatClass(TextInputFormat.class);
        TextInputFormat.addInputPath(job,new Path("D:/bigdatatest/flow/flow_sum_out/part-r-00000"));



        //3、mapper类及参数
        job.setMapperClass(FlowSortMapper.class);
        job.setMapOutputKeyClass(FlowBean.class);
        job.setMapOutputValueClass(Text.class);

        //4、reduce类及参数
        job.setReducerClass(FlowSortReduce.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(FlowBean.class);

        //5、输出
        job.setOutputFormatClass(TextOutputFormat.class);
        TextOutputFormat.setOutputPath(job,new Path("d://bigdatatest/flow/flow_sort_out"));
        //6、等待结束
        boolean b = job.waitForCompletion(true);
        System.out.println(b);
        System.exit(b ? 0 : 1);
    }
}
