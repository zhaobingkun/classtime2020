package com.bigdata.spart;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;

public class TopN {
    SparkConf conf = new SparkConf();
    JavaSparkContext sc = new JavaSparkContext(conf);

}
