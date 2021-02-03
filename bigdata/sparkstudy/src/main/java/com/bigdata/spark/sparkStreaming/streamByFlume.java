package com.bigdata.spark.sparkStreaming;

import org.apache.spark.SparkConf;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaStreamingContext;

public class streamByFlume {
    SparkConf conf = new SparkConf().setAppName("sparkStreaming").setMaster("local[2]");
    JavaStreamingContext jssc = new JavaStreamingContext(conf, Durations.seconds(5));
}
