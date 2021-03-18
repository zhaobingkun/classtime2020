package dmp.tags;

import com.google.inject.internal.cglib.core.$LocalVariablesSorter;
import dmp.beans.Logs;
import org.apache.commons.lang.StringUtils;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.broadcast.Broadcast;
import scala.Tuple2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TagsContext {
    public static void main(String[] args) {
        //判断参数
        /**
         |cn.nx.tags.TagsContext
         |<inputLogPath> 输入日志文件的路径
         |<appMappingPath> app映射文件路径
         |<deviceMappingPath> 设备的映射文件路径
         |<outputPath> 输出路径
         */
        String appMappingPath = "D:\\javas\\bigdata\\appmapping.txt";
        String deviceMappingPath = "D:\\javas\\bigdata\\device_mapping.txt";
        //String inputLogPath = "D:\\javas\\bigdata\\data.txt";
        String inputLogPath = "D:\\javas\\bigdata\\2016.log";
        String outputPath = "D:\\javas\\bigdata\\output\\";
        //接收参数
        //初始化conf对象
        SparkConf conf = new SparkConf();
        conf.setMaster("local").setAppName("usertag");
        conf.set("spark.serializer", "org.apache.Spark.serializer.KryoSerializer");
        conf.registerKryoClasses(new Class[]{Logs.class});

        //初始化程序入口
        JavaSparkContext sc = new JavaSparkContext(conf);
        //生成App广播变量
        JavaPairRDD<String, String> javaPairRDD = sc.textFile(appMappingPath, 1).mapToPair(new PairFunction<String, String, String>() {
            @Override
            public Tuple2<String, String> call(String s) throws Exception {
                String[] fields = s.split("\t");
                return new Tuple2<String, String>(fields[4], fields[1]);
            }
        });

        Map<String, String> appMap = javaPairRDD.collectAsMap();
        Broadcast<Map<String, String>> broadcast = sc.broadcast(appMap);

/*
        for(Map.Entry<String,String> entry : appMap.entrySet()){
            System.out.println(entry.getKey()+","+entry.getValue());
        }
*/


        //生成设备的广播变量
        JavaPairRDD<String, String> deviceJavaPairRDD = sc.textFile(deviceMappingPath).mapToPair(new PairFunction<String, String, String>() {
            @Override
            public Tuple2<String, String> call(String s) throws Exception {
                String[] fields = s.split("\t");
                return new Tuple2<String, String>(fields[0], fields[1]);
            }
        });

        Map<String, String> deviceMap = deviceJavaPairRDD.collectAsMap();
        for (Map.Entry<String, String> entry : deviceMap.entrySet()) {
            System.out.println(entry.getKey() + "," + entry.getValue());
        }


        //读取日志,打标签

        JavaPairRDD<String, List<Tuple2<String, Integer>>> userAndTagsList = sc.textFile(inputLogPath).map(s -> {
            Logs logs = Logs.line2Log(s);
            Map<String, Integer> localTag = Tags4Local.makeTags(logs);
            Map<String, Integer> areaTag = Tags4Area.makeTags(logs);
            Map<String, Integer> channelTag = Tags4Channel.makeTags(logs);
            Map<String, Integer> keyWordsTag = Tags4keyWords.makeTags(logs);
            Map<String, Integer> appTag = Tags4App.makeTags(logs, appMap);
            Map<String, Integer> deviceTag = Tags4Device.makeTags(logs, deviceMap);
            String userid = getNotEmptyID(logs);
            Map<String, Integer> userMap = new HashMap<String, Integer>();
            userMap.putAll(localTag);
            userMap.putAll(areaTag);
            userMap.putAll(channelTag);
            userMap.putAll(keyWordsTag);
            userMap.putAll(appTag);
            userMap.putAll(deviceTag);
            List<Tuple2<String, Integer>> listUserTag = userMap.entrySet().stream().map(
                    et -> {
                        return new Tuple2<String, Integer>(et.getKey(), et.getValue());
                    }).collect(Collectors.toList());

            return new Tuple2<String, List<Tuple2<String, Integer>>>(userid, listUserTag);
        }).filter(new Function<Tuple2<String, List<Tuple2<String, Integer>>>, Boolean>() {
            @Override
            public Boolean call(Tuple2<String, List<Tuple2<String, Integer>>> stringListTuple2) throws Exception {
                return !stringListTuple2._1.equals("");
            }
        }).mapToPair(new PairFunction<Tuple2<String, List<Tuple2<String, Integer>>>, String, List<Tuple2<String, Integer>>>() {
            @Override
            public Tuple2<String, List<Tuple2<String, Integer>>> call(Tuple2<String, List<Tuple2<String, Integer>>> tuple2) throws Exception {
                return tuple2;
            }
        }).reduceByKey(new Function2<List<Tuple2<String, Integer>>, List<Tuple2<String, Integer>>, List<Tuple2<String, Integer>>>() {
            @Override
            public List<Tuple2<String, Integer>> call(List<Tuple2<String, Integer>> tuple2s, List<Tuple2<String, Integer>> tuple2s2) throws Exception {

                List<Tuple2<String, Integer>> listAll = new ArrayList<Tuple2<String, Integer>>();
                listAll.addAll(tuple2s);
                listAll.addAll(tuple2s2);
                return listAll;
            }
        });
                   //.groupByKey();
      // JavaPairRDD<String, Iterable<Tuple2<String, List<Tuple2<String, Integer>>>>> userAndTagsList = /stringIterableJavaPairRDD;

                /*.mapToPair(new PairFunction<Tuple2<String, Iterable<List<Tuple2<String, Integer>>>>, String, List<Tuple2<String, Integer>>>() {
            @Override
            public Tuple2<String, List<Tuple2<String, Integer>>> call(Tuple2<String, Iterable<List<Tuple2<String, Integer>>>> pairs) throws Exception {
                String key = pairs._1();
                Iterable<List<Tuple2<String, Integer>>> iter = pairs._2();
                int sum = 0;

                List<Tuple2<String, Integer>> ll = new ArrayList<Tuple2<String, Integer>>();
                for (List<Tuple2<String, Integer>> i : iter) {
                    for (Tuple2<String, Integer> j : i) {
                        sum += j._2;
                        Tuple2 tt2 = new Tuple2<String, Integer>(j._1, sum);
                        ll.add(tt2);
                    }
                }
                return new Tuple2<String, List<Tuple2<String, Integer>>>(pairs._1, ll);
            }

        });*/

        userAndTagsList.foreach(s->{
            System.out.println(s._1+""+s._2);
        });



}


//后面如何做groupby和sum操作？？
//释放资源



public static String getNotEmptyID(Logs logs){
        String userId="";
        if(StringUtils.isNotEmpty(logs.getImei())){
        userId=logs.getImei();
        }else if(StringUtils.isNotEmpty(logs.getImeimd5())){
        userId=logs.getImeimd5().toUpperCase();
        }else if(StringUtils.isNotEmpty(logs.getImeisha1())){
        userId=logs.getImeisha1().toUpperCase();
        }else if(StringUtils.isNotEmpty(logs.getAndroidid())){
        userId=logs.getAndroidid();
        }else if(StringUtils.isNotEmpty(logs.getAndroididmd5())){
        userId=logs.getAndroididmd5();
        }else if(StringUtils.isNotEmpty(logs.getAndroididsha1())){
        userId=logs.getAndroididsha1();
        }else if(StringUtils.isNotEmpty(logs.getMac())){
        userId=logs.getMac().replaceAll(":|-","");
        }else if(StringUtils.isNotEmpty(logs.getMacmd5())){
        userId=logs.getMacmd5().toUpperCase();
        }else if(StringUtils.isNotEmpty(logs.getMacsha1())){
        userId=logs.getMacsha1().toUpperCase();
        }else if(StringUtils.isNotEmpty(logs.getIdfa())){
        userId=logs.getIdfa().replaceAll(":|-","");
        }else if(StringUtils.isNotEmpty(logs.getIdfamd5())){
        userId=logs.getIdfamd5().toUpperCase();
        }else if(StringUtils.isNotEmpty(logs.getIdfasha1())){
        userId=logs.getIdfasha1().toUpperCase();
        }else if(StringUtils.isNotEmpty(logs.getOpenudid())){
        userId=logs.getOpenudid().toUpperCase();
        }else if(StringUtils.isNotEmpty(logs.getOpenudidmd5())){
        userId=logs.getOpenudidmd5().toUpperCase();
        }else if(StringUtils.isNotEmpty(logs.getOpenudidsha1())){
        userId=logs.getOpenudidsha1().toUpperCase();
        }else{
        userId="";
        }


        return userId;

        }

        }
