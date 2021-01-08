package com.bigdata.mr;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

import java.io.IOException;


/**
 * @author
 * create  2020-11-13 22:30
 * 主类：将Mapper和Reducer阶段串联起来，并且提供了运行的入口
 */
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


    /**
     * 这个main提供了WordCount程序运行的入口，其中用一个Job类对象管理程序运行的很多参数
     * 指定用哪个类作为Mapper的业务逻辑类，指定了那个类作为Reducer的业务逻辑类
     * ......其他的各种需要的参数
     * @param args
     */

    public static void main(String[] args) {

        try{


        //一、初始化换一个Job对象
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf,"WordCount");
        job.setJarByClass(JobMain.class);

        //二、设置Job对象的相关的信息，里面包含了8个小步骤
        //1、设置输入的路径，让程序找到源文件的位置
        job.setInputFormatClass(TextInputFormat.class);
        //TextInputFormat.addInputPath(job,new Path("d://wordcount.txt"));
        TextInputFormat.addInputPath(job,new Path("hdfs://192.168.148.138:8020/wordcount.txt"));



        //2、设置Mapper类型，并设置k2 v2
        job.setMapperClass(WordMapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(LongWritable.class);


        //3 4 5 6 四个步骤，都是Shuffle阶段，现在阶段使用默认的即可

        //7、设置Reducer类型，并设置k3 v3
        job.setReducerClass(WordReduce.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(LongWritable.class);

        //8、设置输出的路径，让结果存放到某个地方去
        job.setOutputFormatClass(TextOutputFormat.class);
        TextOutputFormat.setOutputPath(job,new Path("hdfs://192.168.148.138:8020/word_out2"));
        //TextOutputFormat.setOutputPath(job,new Path("d://word_out2"));
        //三、等待程序完成
        boolean b = job.waitForCompletion(true);
        System.out.println(b);
        System.exit(b ? 0 : 1);
        }
        catch (Exception e){
            System.out.println(e);
        }
    }

}
