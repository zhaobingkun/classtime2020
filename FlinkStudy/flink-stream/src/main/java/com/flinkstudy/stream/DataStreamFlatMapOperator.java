package com.flinkstudy.stream;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

public class DataStreamFlatMapOperator {
    /**
     * flatmap可以理解为将元素摊平，每个元素可以变为0个、1个、或者多个元素。
     * Summary:
     *      FlatMap: 一行变任意行(0~多行)
     */

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        // 输入: 英文电影台词
        DataStreamSource<String> source = env
                .fromElements(
                        "You jump I jump",
                        "Life was like a box of chocolates"
                );

        SingleOutputStreamOperator<String>  result = source.flatMap(new FlatMapFunction<String, String>() {
            @Override
            public void flatMap(String value, Collector<String> out) throws Exception {
                String[] words = value.split(" ");
                for(String word : words){
                    out.collect(word);
                }
            }
        });
        result.print();
        env.execute();

    }
}
