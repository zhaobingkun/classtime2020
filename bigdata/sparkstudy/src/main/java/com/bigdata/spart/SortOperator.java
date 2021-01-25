package com.bigdata.spart;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import scala.Tuple2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
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

        JavaPairRDD<String, Integer> objectObjectJavaPairRDD = pairRDD.mapToPair(new PairFunction<Tuple2<String, Integer>, String, Integer>() {
            @Override
            public Tuple2<String, Integer> call(Tuple2<String, Integer> stringIntegerTuple2) throws Exception {
                Tuple2<String,Integer> tuples = new Tuple2<String,Integer>(stringIntegerTuple2._1,stringIntegerTuple2._2);
                return tuples;
            }
        });

        JavaPairRDD<String, Integer> stringIntegerJavaPairRDD = objectObjectJavaPairRDD.sortByKey();
        stringIntegerJavaPairRDD.foreach(data->{
            System.out.println(data._1+"=="+data._2);
        });
        JavaPairRDD<String, Iterable<Integer>> stringIterableJavaPairRDD = stringIntegerJavaPairRDD.groupByKey();

        stringIterableJavaPairRDD.foreach(data->{

            System.out.println(data._1+"=="+data._2);
        });

        JavaPairRDD<String,Iterable<Integer>>  pairRDD1 = stringIterableJavaPairRDD.mapToPair(new PairFunction<Tuple2<String, Iterable<Integer>>, String, Iterable<Integer>>() {

            @Override
            public Tuple2<String, Iterable<Integer>> call(Tuple2<String, Iterable<Integer>> stringIterableTuple2) throws Exception {
                List<Integer> list = new ArrayList<Integer>();
                Iterable<Integer> views = stringIterableTuple2._2;
                Iterator<Integer> it = views.iterator();
                while(it.hasNext()){
                    Integer view = it.next();
                    list.add(view);
                }
                Collections.sort(list);//正序
                return new Tuple2<String,Iterable<Integer>>(stringIterableTuple2._1,list);
            }
        });

        pairRDD1.foreach(data->{
            System.out.println(data._1+" " +data._2);

        });

    }



}
