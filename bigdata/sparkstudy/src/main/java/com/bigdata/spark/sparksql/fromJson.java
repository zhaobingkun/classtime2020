package com.bigdata.spark.sparksql;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.SQLContext;
import org.apache.spark.sql.SparkSession;

public class fromJson {
    public static void main(String[] args) {
        SparkConf conf = new SparkConf().setMaster("local").setAppName("sql");
        JavaSparkContext sc = new JavaSparkContext(conf);
        SQLContext sqlContext = new SQLContext(sc);
        Dataset df = sqlContext.read().json("E:\\奈学培训\\大数据开发\\第23次\\emp.json");
        df.show();//show默认显示20行，可以跟数字
        //df.printSchema();
        SparkSession spark = SparkSession.builder().config(conf).getOrCreate();
        df.createOrReplaceTempView("emp");
        spark.sql("select * from emp where sal >2000").show();
        }

}
