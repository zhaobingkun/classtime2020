package com.bigdata.spart;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import java.util.Arrays;
import java.util.List;

public class CountOperator {
    public static void main(String[] args) {
        SparkConf conf = new SparkConf();
        conf.setAppName("countOperator").setMaster("local");
        JavaSparkContext sc = new JavaSparkContext(conf);
        List<Integer> numberList = Arrays.asList(1,2,3,4,56);
        //计算RDD中的元素个数
        JavaRDD<Integer> numberRDD = sc.parallelize(numberList);
        long count = numberRDD.count();
        System.out.println(count);
        sc.close();
    }
}
