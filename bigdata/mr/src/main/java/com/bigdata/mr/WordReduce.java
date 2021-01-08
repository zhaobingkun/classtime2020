package com.bigdata.mr;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/**
 * @author LIAO
 * create  2020-11-13 22:21
 * Reducer<KEYIN,VALUEIN,KEYOUT,VALUEOUT>
 *     KEYIN:map阶段输出的key
 *     VALUEIN:数字
 *     KEYOUT:最终的结果的单词，Text类型
 *     VALUEOUT:最终的结果的单词的次数，LongWritable类型
 */
public class WordReduce extends Reducer<Text, LongWritable, Text, LongWritable> {
    /**
     *
     * @param key 单词
     * @param values 相同的单词的次数
     * @param context
     * @throws IOException
     * @throws InterruptedException
     */
    @Override
    protected void reduce(Text key, Iterable<LongWritable> values, Context context) throws IOException, InterruptedException {

        //1、定义一个统计的变量
        long count=0;

        //2、迭代
        for (LongWritable value:values){
            count+=value.get();
        }
        //3、写入到上下文
        context.write(key,new LongWritable(count));
    }
}
