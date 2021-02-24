package com.bigdata.flink;

import com.bigdata.flink.model.WordCountModel;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;


public class WordCountByModel {

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(new Configuration());
        ParameterTool param =  ParameterTool.fromArgs(args);

        //String hostname = param.get("hostname");
        //int port = param.getInt("port");

        String hostname="hadoop31";
        int port = 1234;
        DataStreamSource<String> dataStreamSource = env.socketTextStream(hostname, port);
        SingleOutputStreamOperator<WordCountModel> resule = dataStreamSource.flatMap(new FlatMapFunction<String, WordCountModel>() {
            @Override
            public void flatMap(String s, Collector<WordCountModel> collector) throws Exception {
                String[] fields = s.split(",");
                for(String word:fields){
                    collector.collect(new WordCountModel(word,1));
                }
            }
        }).keyBy("word").sum("count");

        resule.print();
        env.execute("WordCount...");


    }



}
