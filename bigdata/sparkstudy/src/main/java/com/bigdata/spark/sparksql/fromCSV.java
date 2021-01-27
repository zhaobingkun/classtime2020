package com.bigdata.spark.sparksql;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.SQLContext;

public class fromCSV {

    public static void main(String[] args) {
        SparkConf conf = new SparkConf().setMaster("local").setAppName("sql");
        JavaSparkContext sc = new JavaSparkContext(conf);
        SQLContext sqlContext = new SQLContext(sc);
        Dataset df = sqlContext.read().csv("E:\\奈学培训\\大数据开发\\第23次\\dept.csv");
        df.show();//show默认显示20行，可以跟数字
        df.printSchema();
    }
}
