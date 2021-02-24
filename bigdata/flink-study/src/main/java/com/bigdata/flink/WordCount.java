package com.bigdata.flink;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;
import org.apache.flink.api.java.tuple.Tuple2;

public class WordCount {



    public static void main(String[] args) throws Exception {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStreamSource<String> dStream = env.socketTextStream("hadoop31", 1234);
        SingleOutputStreamOperator<Tuple2<String, Integer>> wordCount = dStream.flatMap(new FlatMapFunction<String, Tuple2<String, Integer>>() {
            @Override
            public void flatMap(String s, Collector<Tuple2<String, Integer>> collector) throws Exception {
                String[] field = s.split(",");
                for(String word:field){
                    collector.collect(new Tuple2<String,Integer>(word,1));
                }
            }
        }).keyBy(0).sum(1);

        wordCount.print();
        env.execute("word count");

    }




}
