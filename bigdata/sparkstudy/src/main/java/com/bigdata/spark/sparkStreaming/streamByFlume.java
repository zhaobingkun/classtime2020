package com.bigdata.spark.sparkStreaming;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.storage.StorageLevel;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaReceiverInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.flume.FlumeUtils;
import org.apache.spark.streaming.flume.SparkFlumeEvent;

public class streamByFlume {
    public static void main(String[] args) throws InterruptedException {
        SparkConf conf = new SparkConf().setAppName("sparkStreaming").setMaster("local[2]");
        JavaStreamingContext jssc = new JavaStreamingContext(conf, Durations.seconds(5));
        JavaReceiverInputDStream<SparkFlumeEvent> flumeEvent =  FlumeUtils.createPollingStream(jssc,"192.168.148.131",1234, StorageLevel.MEMORY_ONLY());
        JavaDStream<String> map = flumeEvent.map(new Function<SparkFlumeEvent, String>() {
            @Override
            public String call(SparkFlumeEvent sparkFlumeEvent) throws Exception {
                return sparkFlumeEvent.event().getBody().array().toString();
            }
        });

        map.print();
        jssc.start();
        jssc.awaitTermination();
        jssc.close();
    }


}
