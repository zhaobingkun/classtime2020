package com.bigdata.spart;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.PairFunction;
import scala.Tuple2;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;




public class mapValueOperator {
    public static void main(String[] args) {
        SparkConf conf =new SparkConf().setMaster("local").setAppName("mapValue");
        JavaSparkContext sc = new JavaSparkContext(conf);
        List<Tuple2<String,Integer>> scoreList = Arrays.asList(
                new Tuple2<String, Integer>("spark",2),
                new Tuple2<String, Integer>("hadoop",6),
                new Tuple2<String, Integer>("spark",6),
                new Tuple2<String, Integer>("hadoop",4)
        );

        List<Tuple2<String,Integer>> inputList = new ArrayList<Tuple2<String,Integer>>();
        inputList.add(new Tuple2<String,Integer>("spark", 2));
        inputList.add(new Tuple2<String,Integer>("hadoop", 6));
        inputList.add(new Tuple2<String,Integer>("spark", 6));
        inputList.add(new Tuple2<String,Integer>("hadoop", 4));

        JavaPairRDD<String, Integer> pairRDD = sc.<String, Integer>parallelizePairs(inputList);

        JavaPairRDD<String, Tuple2<Integer, Integer>> stringTuple2JavaPairRDD = pairRDD.mapValues(value -> new Tuple2<Integer, Integer>(value, 1));

        stringTuple2JavaPairRDD.foreach(data -> {
            System.out.println("Key="+data._1() + " Average=" + data._2());
        });

        JavaPairRDD<String, Tuple2<Integer, Integer>> stringTuple2JavaPairRDD1 = stringTuple2JavaPairRDD.reduceByKey((tuple1, tuple2) -> new Tuple2<Integer, Integer>(tuple1._1 + tuple2._1, tuple1._2 + tuple2._2));

        stringTuple2JavaPairRDD1.foreach(data -> {
            System.out.println("Key="+data._1() + " Average=" + data._2());
        });

        JavaPairRDD<String, Integer> stringIntegerJavaPairRDD = stringTuple2JavaPairRDD1.mapToPair(getAverageByKey);

        stringIntegerJavaPairRDD.foreach(data -> {
            System.out.println("Key="+data._1() + " Average=" + data._2());
        });
        //stop sc
        sc.stop();
        sc.close();
    }


    private static PairFunction<Tuple2<String, Tuple2<Integer, Integer>>,String,Integer> getAverageByKey = (tuple) -> {
        Tuple2<Integer, Integer> val = tuple._2;
        int total = val._1;
        int count = val._2;
        Tuple2<String, Integer> averagePair = new Tuple2<String, Integer>(tuple._1, total / count);
        return averagePair;
    };
}
