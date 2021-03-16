package dmp.tools;


import dmp.beans.Logs;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

public class Tools {


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
    //3.配置序列化方式
    //4.创建SparkSession对象
    //5.读取文件,进行相应操作
    //6.存储结果文件
    //7.释放资源

    /**
     * <dataPath><outputPath><compressionCode>
     *       |<dataPath> 日志所在的路径
     *       |<outputPath> 结果文件存放的路径
     *       |<compressionCode> 指定的压缩格式
     * @param args
     */
    public static void main(String[] args) {
        //1.判断参数个数
        //if(args.length<3){
        //    System.out.println("参数错误");
        //}

        //2.接收参数
        //String dataPath = args[0];
        String dataPath = "D:\\javas\\bigdata\\2016.log";
        //String outputPath = args[1];
        String outputPath = "D:\\sparkbigdata_log\\";
        //String compressionCode = args[2];
        String compressionCode="Snappy";
        //3.配置序列化方式

        SparkConf conf = new SparkConf();
        conf.setAppName("outputfile");
        conf.set("spark.serializer","org.apache.Spark.serializer.KryoSerializer");
        conf.registerKryoClasses(new Class[]{Logs.class});
        conf.set("spark.io.compression.codec",compressionCode);
        conf.setMaster("local");

        //4.创建SparkSession对象
        SparkSession spark = SparkSession.builder().config(conf).getOrCreate();
        //5.读取文件,进行相应操作

        JavaRDD<Logs> logsRDD = spark.read().textFile(dataPath).javaRDD().map(s -> Logs.line2Log(s));

        //spark.read().textFile(dataPath).javaRDD().flatMap(s -> );
        /*
        JavaRDD<Logs> logsRDD = spark.read().textFile(dataPath).javaRDD().map(new Function<String, Logs>() {
            @Override
            public Logs call(String s) throws Exception {
                return Logs.line2Log(s);
            }
        });*/

        Dataset<Row> df =spark.createDataFrame(logsRDD,Logs.class);
        //df.show();
        df.write().parquet(outputPath);
        spark.close();


    }
}
