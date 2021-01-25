package com.bigdata.spark;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import scala.Tuple2;
import java.util.ArrayList;
import java.util.List;


/**
 * MapValues算子
 * 传进来的值，传出去的格式可以自己设置，亦即可以定义
 * 可以将tuple2中value的值加起来,相当于reduceByKey
 * 也可以将key的个数加起来，相当于countByKey
 *
 * @author zhaobk
 *
 */

public class mapValueOperator {



    public static void main(String[] args) {
        SparkConf conf =new SparkConf().setMaster("local").setAppName("mapValue");
        JavaSparkContext sc = new JavaSparkContext(conf);
        List<Tuple2<String,Integer>> inputList = new ArrayList<Tuple2<String,Integer>>();
        inputList.add(new Tuple2<String,Integer>("spark", 2));
        inputList.add(new Tuple2<String,Integer>("hadoop", 6));
        inputList.add(new Tuple2<String,Integer>("spark", 6));
        inputList.add(new Tuple2<String,Integer>("hadoop", 4));

        JavaPairRDD<String, Integer> pairRDD = sc.<String, Integer>parallelizePairs(inputList);


        JavaPairRDD<String, Integer> stringIntegerJavaPairRDD1 = pairRDD.reduceByKey(new Function2<Integer, Integer, Integer>() {
            @Override
            public Integer call(Integer integer, Integer integer2) throws Exception {
                return integer+integer2;
            }
        });
        stringIntegerJavaPairRDD1.foreach(data->{
            System.out.println(data._1+" ==  " + data._2);
        });


        JavaPairRDD<String, Tuple2<Integer, Integer>> stringTuple2JavaPairRDD = pairRDD.mapValues(new Function<Integer, Tuple2<Integer, Integer>>() {
            @Override
            public Tuple2<Integer, Integer> call(Integer integer) throws Exception {
                return new Tuple2<Integer,Integer>(integer,1);
            }
        });

        //pairRDD.mapValues(value -> new Tuple2<Integer, Integer>(value, 1));


        stringTuple2JavaPairRDD.foreach(data -> {
            System.out.println("Key="+data._1() + " Average=" + data._2());
        });

        //JavaPairRDD<String, Tuple2<Integer, Integer>> stringTuple2JavaPairRDD1 = stringTuple2JavaPairRDD.reduceByKey((tuple1, tuple2) -> new Tuple2<Integer, Integer>(tuple1._1 + tuple2._1, tuple1._2 + tuple2._2));

        JavaPairRDD<String, Tuple2<Integer, Integer>> stringTuple2JavaPairRDD1 = stringTuple2JavaPairRDD.reduceByKey(new Function2<Tuple2<Integer, Integer>, Tuple2<Integer, Integer>, Tuple2<Integer, Integer>>() {
            @Override
            public Tuple2<Integer, Integer> call(Tuple2<Integer, Integer> integerIntegerTuple1, Tuple2<Integer, Integer> integerIntegerTuple2) throws Exception {
                return new Tuple2<Integer, Integer>(integerIntegerTuple1._1 + integerIntegerTuple2._1, integerIntegerTuple1._2 + integerIntegerTuple2._2);
            }
        });




        stringTuple2JavaPairRDD1.foreach(data -> {
            System.out.println("Key="+data._1() + " Average=" + data._2());
        });

        JavaPairRDD<String, Integer> stringIntegerJavaPairRDD = stringTuple2JavaPairRDD1.mapToPair(new PairFunction<Tuple2<String, Tuple2<Integer, Integer>>, String, Integer>() {
            @Override
            public Tuple2<String, Integer> call(Tuple2<String, Tuple2<Integer, Integer>> stringTuple2Tuple2) throws Exception {
                Tuple2<Integer, Integer> val = stringTuple2Tuple2._2;
                int total = val._1;
                int count = val._2;
                Tuple2<String, Integer> averagePair = new Tuple2<String, Integer>(stringTuple2Tuple2._1, total / count);
                return averagePair;
            }
        });

        stringIntegerJavaPairRDD.foreach(data -> {
            System.out.println("Key="+data._1() + " Average=" + data._2());
        });
        sc.stop();
        sc.close();
    }

}
