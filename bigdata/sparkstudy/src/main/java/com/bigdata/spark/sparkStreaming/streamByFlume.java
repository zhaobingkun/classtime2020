package com.bigdata.spark.sparkStreaming;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.storage.StorageLevel;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaReceiverInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.flume.FlumeUtils;
import org.apache.spark.streaming.flume.SparkFlumeEvent;
import scala.Tuple2;

import java.util.Arrays;
import java.util.Iterator;

public class streamByFlume {
    public static void main(String[] args) throws InterruptedException {
        SparkConf conf = new SparkConf().setAppName("sparkStreaming").setMaster("local[2]");
        JavaStreamingContext jssc = new JavaStreamingContext(conf, Durations.seconds(5));
        JavaReceiverInputDStream<SparkFlumeEvent> flumeEvent =  FlumeUtils.createPollingStream(jssc,"192.168.148.131",1234, StorageLevel.MEMORY_ONLY());

        JavaDStream<String> map = flumeEvent.map(new Function<SparkFlumeEvent, String>() {
            @Override
            public String call(SparkFlumeEvent sparkFlumeEvent) throws Exception {
                //得到的是字符数组，转成字符串
                String bodyString = new String(sparkFlumeEvent.event().getBody().array(), "UTF-8");
                return bodyString;
            }
        });

        map.print();

        //压平
        JavaDStream<String> stringJavaDStream = map.flatMap(new FlatMapFunction<String, String>() {
            @Override
            public Iterator<String> call(String s) throws Exception {
                return Arrays.asList(s.split(" ")).iterator();
            }
        });

        stringJavaDStream.print();

        //
        JavaPairDStream<String, Integer> stringIntegerJavaPairDStream = stringJavaDStream.mapToPair(new PairFunction<String, String, Integer>() {
            @Override
            public Tuple2<String, Integer> call(String s) throws Exception {
                System.out.println("s=="+s);
                return new Tuple2<String, Integer>(s, 1);
            }
        });

        stringIntegerJavaPairDStream.print();

        JavaPairDStream<String, Integer> stringIntegerJavaPairDStream1 = stringIntegerJavaPairDStream.reduceByKey(new Function2<Integer, Integer, Integer>() {
            @Override
            public Integer call(Integer integer, Integer integer2) throws Exception {
                return integer + integer2;
            }
        });

        stringIntegerJavaPairDStream1.print();

        jssc.start();
        jssc.awaitTermination();
        jssc.close();
    }


}
