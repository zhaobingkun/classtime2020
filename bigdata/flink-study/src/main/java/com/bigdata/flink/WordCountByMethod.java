package com.bigdata.flink;

import com.bigdata.flink.flinkmethod.FlatMapMethod;
import com.bigdata.flink.model.WordCountModel;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class WordCountByMethod {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironment();
        DataStreamSource<String> DStreamSource = env.socketTextStream("hadoop31", 1234);
        SingleOutputStreamOperator<WordCountModel> result = DStreamSource.flatMap(new FlatMapMethod() {
        }).keyBy("word").sum("count");
        result.print();
        env.execute("word count");

    }


}
