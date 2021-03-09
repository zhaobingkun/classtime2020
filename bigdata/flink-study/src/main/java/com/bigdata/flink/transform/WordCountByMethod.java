package com.bigdata.flink.transform;

import com.bigdata.flink.flinkmethod.FlatMapMethod;
import com.bigdata.flink.model.WordCountModel;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class WordCountByMethod {
    public static void main(String[] args) throws Exception {
        //StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironment();

        //DataStreamSource<String> DStreamSource = env.socketTextStream("hadoop31", 1234);


        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        /**
         * 针对的是整个全局
         */
        env.setParallelism(2);

        ParameterTool parameterTool = ParameterTool.fromArgs(args);
        String hostname = parameterTool.get("hostname");
        int port = parameterTool.getInt("port");

        DataStreamSource<String> dataStream = env.socketTextStream(hostname, port);

        SingleOutputStreamOperator<WordCountModel> result = dataStream.flatMap(new FlatMapMethod() {
        }).keyBy("word").sum("count");
        result.print();
        env.execute("word count");

    }


}
