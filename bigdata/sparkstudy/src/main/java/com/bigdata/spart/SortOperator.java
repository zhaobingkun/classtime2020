package com.bigdata.spart;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

import java.util.ArrayList;
import java.util.List;

public class SortOperator {
    public static void main(String[] args) {
        SparkConf conf = new SparkConf().setAppName("sort").setMaster("local");
        JavaSparkContext sc = new JavaSparkContext(conf);

        /**
         *     hadoop@apache          200
         *     hive@apache            550
         *     yarn@apache            580
         *     hive@apache            159
         *     hadoop@apache          300
         *     hive@apache            258
         *     hadoop@apache          150
         *     yarn@apache            560
         *     yarn@apache            260
         */

        List<Tuple2<String,Integer>> inputList = new ArrayList<Tuple2<String,Integer>>();
        inputList.add(new Tuple2<String,Integer>("hadoop@apache", 200));
        inputList.add(new Tuple2<String,Integer>("hive@apache", 550));
        inputList.add(new Tuple2<String,Integer>("yarn@apache", 580));
        inputList.add(new Tuple2<String,Integer>("hive@apache", 159));
        inputList.add(new Tuple2<String,Integer>("hadoop@apache", 300));
        inputList.add(new Tuple2<String,Integer>("hive@apache", 258));
        inputList.add(new Tuple2<String,Integer>("hadoop@apache", 150));
        inputList.add(new Tuple2<String,Integer>("yarn@apache", 560));
        inputList.add(new Tuple2<String,Integer>("yarn@apache", 260));


        JavaRDD<Tuple2<String, Integer>> pairRDD = sc.parallelize(inputList);

        pairRDD.foreach(data->{
            System.out.println(data._1+"=="+data._2);
        });


    }



}
