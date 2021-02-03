package com.bigdata.spark.sparkStreaming;


import org.apache.spark.SparkConf;
import org.apache.spark.storage.StorageLevel;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.*;
import org.apache.spark.api.java.function.*;
import org.apache.spark.streaming.*;
import org.apache.spark.streaming.api.java.*;
import scala.Tuple2;

import java.util.Arrays;
import java.util.Iterator;

public class networkWordCount {
    public static void main(String[] args) throws InterruptedException {
        SparkConf conf = new SparkConf().setAppName("sparkStreaming").setMaster("local[2]");
        JavaStreamingContext jssc = new JavaStreamingContext(conf, Durations.seconds(5));
        System.out.println(jssc);
        //nc -l -p 1234
        JavaReceiverInputDStream<String> lines = jssc.socketTextStream("192.168.148.131", 1234, StorageLevel.MEMORY_ONLY());

        JavaDStream<String> stringJavaDStream = lines.flatMap(new FlatMapFunction<String, String>() {
            @Override
            public Iterator<String> call(String s) throws Exception {
                return Arrays.asList(s.split(" ")).iterator();
            }
        });


        JavaPairDStream<String, Integer> stringIntegerJavaPairDStream = stringJavaDStream.mapToPair(new PairFunction<String, String, Integer>() {
            @Override
            public Tuple2<String, Integer> call(String s) throws Exception {
                return new Tuple2<String, Integer>(s, 1);
            }
        });

        JavaPairDStream<String, Integer> stringIntegerJavaPairDStream1 = stringIntegerJavaPairDStream.reduceByKey(new Function2<Integer, Integer, Integer>() {
            @Override
            public Integer call(Integer integer, Integer integer2) throws Exception {
                return integer + integer2;
            }
        });

        stringIntegerJavaPairDStream1.print();
        jssc.start();
        jssc.awaitTermination();

    }
}
