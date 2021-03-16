package dmp.reportOffline;


import com.codepoetics.protonpack.StreamUtils;
import dmp.beans.Logs;
import dmp.tools.ReportUtils;
import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import scala.Tuple2;
import scala.Tuple3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ProvinceCityReport {
    public static void main(String[] args) {
        //1.判断参数个数
        /**
         *
         *  |cn.nx.reportOffline.ProvinceCityReport
         |<logInputPath> 文件输入目录
         |<provinceDataPath> 省份结果文件目录
         |<cityDataPath> 城市结果文件目录
         *
         * */

        String logInputPath = "D:\\javas\\bigdata\\2016.log";
        //String logInputPath = "D:\\javas\\bigdata\\data.txt";
        String provinceDataPath = "D:\\sparkbigdata_provinceData\\";
        String cityDataPath = "D:\\sparkbigdata_cityData\\";
        //2.接收参数

        //3.添加配置选项
        SparkConf conf = new SparkConf();
        conf.setAppName("dmp").setMaster("local");
        conf.set("spark.serializer", "org.apache.Spark.serializer.KryoSerializer");
        conf.registerKryoClasses(new Class[]{Logs.class});

        //4.创建一个SparkContext对象
        SparkContext sc = new SparkContext(conf);

        //5.逻辑代码处理
        JavaRDD<Tuple3<String, String, List>> logsRDD = sc.textFile(logInputPath, 1).toJavaRDD().map(s -> {
            Logs logs = Logs.line2Log(s);

            List adRequest = ReportUtils.calculateRequest(logs);
            List adResponse = ReportUtils.calculateResponse(logs);
            List adClick = ReportUtils.calculateShowClick(logs);
            List adCost = ReportUtils.calculateAdCost(logs);
            Tuple3<String, String, List> logss = new Tuple3<String, String, List>(logs.getProvincename(), logs.getCityname(), adRequest);
            return logss;
        });
        // 计算各省份的指标
/*

        JavaPairRDD<String, List> map = logsRDD.mapToPair(new PairFunction<Tuple3<String, String, List>, String, List>() {
            @Override
            public Tuple2<String, List> call(Tuple3<String, String, List> stringStringListTuple3) throws Exception {
                return new Tuple2<String,List>(stringStringListTuple3._1(),stringStringListTuple3._3());
            }
        });

        JavaPairRDD<String, List> stringListJavaPairRDD1 = map.reduceByKey(new Function2<List, List, List>() {
            @Override
            public List call(List list, List list2) throws Exception {


                Stream<Integer> streamA = list.stream();
                Stream<Integer> streamB  = list2.stream();
                List<Integer> collect = StreamUtils.zip(streamA,streamB,(a, b) -> a+b).collect(Collectors.toList());

                return collect;
            }
        });

        stringListJavaPairRDD1.foreach(s->{
            System.out.println(s._1 + "," + s._2);
        });

*/


        //计算各省份城市的指标

        JavaPairRDD<String, List> mapCity = logsRDD.mapToPair(new PairFunction<Tuple3<String, String, List>, String, List>() {
            @Override
            public Tuple2<String, List> call(Tuple3<String, String, List> tuple) throws Exception {
                return new Tuple2<String,List>(tuple._1() + tuple._2(),tuple._3());
            }
        });


        JavaPairRDD<String, List> cityRDD = mapCity.reduceByKey(new Function2<List, List, List>() {
            @Override
            public List call(List list, List list2) throws Exception {
                Stream<Integer> streamA = list.stream();
                Stream<Integer> streamB  = list2.stream();
                List<Integer> collect = StreamUtils.zip(streamA,streamB,(a, b) -> a+b).collect(Collectors.toList());

                return collect;
            }
        });

        cityRDD.foreach(s->{
            System.out.println(s._1 + "," + s._2);
        });

        //6.释放资源
        sc.stop();
    }




/*
        JavaPairRDD<String, List> stringListJavaPairRDD = map
                mapToPair(new PairFunction<Tuple2<String, List>, String, List>() {
            @Override
            public Tuple2<String, List> call(Tuple2<String, List> stringListTuple2) throws Exception {
                return stringListTuple2;
            }
        }).reduceByKey(new Function2<List, List, List>() {
            @Override
            public List call(List list, List list2) throws Exception {
                return null;
            }
        });*/



}

