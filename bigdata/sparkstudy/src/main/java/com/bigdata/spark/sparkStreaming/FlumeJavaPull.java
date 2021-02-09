package com.bigdata.spark.sparkStreaming;


import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaReceiverInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.flume.FlumeUtils;
import org.apache.spark.streaming.flume.SparkFlumeEvent;
import scala.Tuple2;

import java.util.Arrays;
import java.util.Iterator;




public class FlumeJavaPull {

    //将不可见字符\X07转换成","  \X06转换成""
    public static String ascii2Str(String str){
        String newStr = "";
        char[] cs = str.toCharArray();
        int j = 0;
        for(int i : cs){
            if(i == 7){
                newStr += ",";
            }else if(i==6){
                newStr += "";
            }else{
                newStr += str.charAt(j);
            }
            j++;
        }
        return newStr;
    }

    public static void main(String[] args) throws InterruptedException {
        Logger.getLogger("org.apache.spark").setLevel(Level.ERROR);

        SparkConf conf = new SparkConf().setAppName("aaa").setMaster("local[2]");
        JavaStreamingContext ssc = new JavaStreamingContext(conf, new Duration(Integer.parseInt("5000")));
        JavaReceiverInputDStream<SparkFlumeEvent> pollingStream = FlumeUtils.createPollingStream(ssc, "192.168.148.131", 1234);
        JavaDStream<String> map = pollingStream.map(new Function<SparkFlumeEvent, String>() {
            @Override
            public String call(SparkFlumeEvent sparkFlumeEvent) throws Exception {
                String bodyString = new String(sparkFlumeEvent.event().getBody().array(), "UTF-8");
                System.out.println(bodyString);
                return bodyString;

            }
        });


        //进行分词操作
        JavaDStream<String> words = map.flatMap(new FlatMapFunction<String, String>() {
            @Override
            public Iterator<String> call(String input) throws Exception {


                return Arrays.asList(input.split(" ")).iterator();
            }
        });

       // words.print();

        JavaPairDStream<String, Integer> wordcount = words.mapToPair(new PairFunction<String, String, Integer>() {
            @Override
            public Tuple2<String, Integer> call(String word) {
                System.out.println("word= "+word+" len="+word.length()+"\n");
                //String newStr =  ascii2Str(word);

                //System.out.print("new str == "+newStr+"\n");
                return new Tuple2<String, Integer>(word, 1);
            }
        });

        wordcount.print();

        JavaPairDStream<String, Integer> result = wordcount.reduceByKey(new Function2<Integer, Integer, Integer>() {
            @Override
            public Integer call(Integer a, Integer b) throws Exception {
                return a + b;
            }
        });

        //打印结果
        result.print();
        ssc.start();
        ssc.awaitTermination();

    }



}
