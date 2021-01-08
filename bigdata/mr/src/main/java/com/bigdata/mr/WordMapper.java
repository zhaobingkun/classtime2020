package com.bigdata.mr;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class WordMapper extends Mapper<LongWritable, Text, Text, LongWritable> {

    /**
     * @author
     * create  2020-11-13 21:55
     * Mapper<KEYIN, VALUEIN, KEYOUT, VALUEOUT>
     *     KEYIN:是指框架读取到的数据的key的类型，在默认的情况下，读取到的key就是一行数据相对于整个文本起始位置的偏移量。key的类型是不是可以为Long
     *     VALUEIN:是指框架读取到的数据的value的类型，在默认的情况下，读取到的value就是一行数据。value的类型是不是可以为String
     *     KEYOUT:是指用户自定义的逻辑方法返回的数据当中的key的类型，由用户根据业务逻辑自己决定的，在我们的WordCount这个程序中，这个key就是单词。key是不是可以为String
     *     VALUEOUT:是指用户自定的逻辑方法返回的数据当中的value的类型，由用户根据业务逻辑自己决定的，在我们的WordCount程序中，这个value就是次数。value是不是可以为Long
     *
     *      但是，String、Long是jdk里面的数据类型，在序列化的时候，效率低
     *      hadoop为了提高效率，自定义了一套序列化的框架
     *      在hadoop的程序中，如果要进行序列化（写磁盘、网络传输等），一定要使用hadoop实现的序列化的数据类型
     *
     *      Long ——》LongWritable
     *      String ——》 Text
     *      Integer ——》 IntWritable
     *      Null ——》 NullWritable
     *
     */


    /**
     * @param key 就是偏移量
     * @param value 一行文本数据
     * @param context 上下文
     * @throws IOException
     * @throws InterruptedException
     */
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        //1、单词的切分
       String[] words = value.toString().split(" ");
        //2、计数一次，帮单词转换成类似于<hello,1>这样的key value类型对外输出
       for (String word:words){
           // //3、写入到上下文
           System.out.println(word);
           context.write(new Text(word),new LongWritable(1));
       }
    }
}
