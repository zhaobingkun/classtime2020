package com.bigdata.spark;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.PairFunction;
import scala.Tuple2;

import java.util.List;

public class TopN {
    public static void main(String[] args) {
        SparkConf conf = new SparkConf();
        conf.setMaster("local").setAppName("topn");
        JavaSparkContext sc = new JavaSparkContext(conf);
        //JavaRDD<String> stringJavaRDD = sc.textFile("D:\\javas\\txtfile\\file1.txt");
        JavaRDD<String> stringJavaRDD = sc.textFile("D:\\javas\\txtfile"); //读入文件


        JavaPairRDD<Integer, String> objectObjectJavaPairRDD = stringJavaRDD.mapToPair(new PairFunction<String, Integer, String>() {
            @Override
            public Tuple2<Integer, String> call(String s) throws Exception {
                String[] tokens = s.split(",");
                return new Tuple2<Integer,String>(Integer.parseInt(tokens[2]), s);
            }
        });

        objectObjectJavaPairRDD.foreach(data->{
            System.out.println(data._1 + "-----" +data._2) ;
        });

        List<Tuple2<Integer, String>> result = objectObjectJavaPairRDD.sortByKey(false).take(5);
        for(Tuple2<Integer, String> rs:result){
            System.out.println(rs._1+ "==" +rs._2);

        }


    }



}
