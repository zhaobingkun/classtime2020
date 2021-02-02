package com.bigdata.spark.sparksql;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.SQLContext;
import org.apache.spark.sql.SparkSession;

public class sparkScore {

    public static void main(String[] args) {

        SparkConf conf = new SparkConf().setMaster("local").setAppName("sql");
        JavaSparkContext sc = new JavaSparkContext(conf);
        SQLContext sqlContext = new SQLContext(sc);
        Dataset df = sqlContext.read().json("E:\\奈学培训\\大数据开发\\第24次\\score.json");
        df.show();//show默认显示20行，可以跟数字
        df.printSchema();
        SparkSession spark = SparkSession.builder().config(conf).getOrCreate();
        df.createOrReplaceTempView("score");
        spark.sql("select  t1.course course,  t1.name name,  t1.score score  from ( select  course,  name,  score,  row_number() over(partition by course order by score desc ) top  from score) t1 where t1.top<=3").show();



    }
}
