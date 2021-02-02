package com.bigdata.spark.sparkStreaming;


import org.apache.spark.SparkConf;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.*;
import org.apache.spark.api.java.function.*;
import org.apache.spark.streaming.*;
import org.apache.spark.streaming.api.java.*;

public class flumeStreamPull {
    public static void main(String[] args) {
        SparkConf conf = new SparkConf().setAppName("sparkStreaming").setMaster("local[2]");
        JavaStreamingContext jssc = new JavaStreamingContext(conf, Durations.seconds(1));
        System.out.println(jssc);


    }
}
