package dmp.reportOffline;


import dmp.beans.Logs;
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

        String inputFilePath = "D:\\sparkbigdata_log";
        //String outputPath = args[1];
        String outputFilePath = "D:\\sparkbigdata_city_json\\";

        SparkConf conf = new SparkConf();
        conf.setAppName("dmp").setMaster("local");

        SparkSession spark = SparkSession.builder().config(conf).config("spark.debug.maxToStringFields", "100").getOrCreate();
        Dataset<Row> ds = spark.read().parquet(inputFilePath);
        //Dataset<String> ds = spark.read().textFile();
        //ds.schema();
        //ds.printSchema();
        ds.createOrReplaceTempView("logs");
        String sql = "select count(*) ct,provincename,cityname from logs where requestmode =1 and (processnode=1 or processnode=2)  group by provincename,cityname order by provincename";
        //String sql = "select  provincename from logs where requestmode =1 and processnode=1  order by provincename";

        //String sql = "select  bidprice,winprice from logs  where bidprice>0 or winprice>0  limit 20";
        spark.sql(sql).repartition(1).write().json(outputFilePath);

        //spark.sql(sql).show(100);

        spark.stop();




    }
}
