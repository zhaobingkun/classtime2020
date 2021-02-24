package com.bigdata.flink;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer010;
import org.apache.flink.util.Collector;

import java.util.Properties;

public class WordCountByKafka {

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env  = StreamExecutionEnvironment.createLocalEnvironment();
        String topic = "FlinkStudy-01";
        Properties consumerProperties = new Properties();
        consumerProperties.setProperty("bootstrap.servers","hadoop31:9092,hadoop32:9092,hadoop33:9092");
        consumerProperties.setProperty("group.id","flinkstudy01_consumer");
        FlinkKafkaConsumer010<String> myConsumer =
                new FlinkKafkaConsumer010<>(topic, new SimpleStringSchema(), consumerProperties);

        DataStreamSource<String> data = env.addSource(myConsumer).setParallelism(3);
        SingleOutputStreamOperator<Tuple2<String, Integer>>  wordCount = data.flatMap(new FlatMapFunction<String, Tuple2<String, Integer>>() {
            @Override
            public void flatMap(String lines, Collector<Tuple2<String, Integer>> out) throws Exception {
                String[] fields = lines.split(",");
                for (String word : fields) {
                    out.collect(new Tuple2<String, Integer>(word, 1));
                }
            }
        }).setParallelism(3);
        SingleOutputStreamOperator<Tuple2<String, Integer>> result = wordCount.keyBy(0).sum(1).setParallelism(3);
        result.print().setParallelism(1);
        env.execute("word count");


    }


}
