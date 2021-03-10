package com.bigdata.dmp.reportOffline;


import org.apache.spark.SparkConf;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;




/**
 * 统计各省市数据量分布
 */
public class ProvinceCityAnalyse {

    static {
        try {
            // 设置 HADOOP_HOME 目录
            System.setProperty("hadoop.home.dir", "E:\\download\\dllwinutils");
            // 加载库文件
            System.load("E:\\download\\dllwinutils\\hadoop.dll");
        } catch (UnsatisfiedLinkError e) {
            System.err.println("Native code library failed to load.\n" + e);
            System.exit(1);
        }
    }

    //1.判断参数个数
    //2.接收参数
    //3.添加配置参数
    //4.读取文件,做逻辑代码开发
    //5.存储结果文件
    //6.释放资源

    public static void main(String[] args) {
        /**
         *
         *           |reportOffline.ProvinceCityAnalyse <inputFilePath><outputFilePath>
         *           |<inputFilePath> 输入文件路径
         *           |<outputFilePath> 输出文件路径
         *           |)
         */

        String inputFilePath = "D:\\sparkbigdata";
        //String outputPath = args[1];
        String outputFilePath = "D:\\sparkbigdata3\\";

        SparkConf conf = new SparkConf();
        conf.setAppName("dmp").setMaster("local");
        SparkSession spark = SparkSession.builder().config(conf).getOrCreate();
        Dataset<Row> ds = spark.read().parquet(inputFilePath);
        ds.printSchema();
        ds.createOrReplaceTempView("logs");
        String sql = "select count(*) ct,provincename,cityname from logs group by provincename,cityname order by provincename";
        spark.sql(sql).repartition(1).write().json(outputFilePath);

        //spark.sql(sql).show();

        spark.stop();




    }
}
