package com.cmei.s2r;

import com.ververica.cdc.connectors.sqlserver.SqlServerSource;
import com.ververica.cdc.debezium.JsonDebeziumDeserializationSchema;
import org.apache.flink.api.common.restartstrategy.RestartStrategies;
import org.apache.flink.runtime.state.memory.MemoryStateBackend;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.environment.CheckpointConfig;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;
import org.apache.flink.streaming.api.functions.source.SourceFunction;

public class sql2redis {

    public static void main(String[] args) throws Exception {
        SourceFunction<String> sourceFunction = SqlServerSource.<String>builder()
                .hostname("192.168.10.134")
                .port(1433)
                .database("inventory") // monitor sqlserver database
                .tableList("dbo.products") // monitor products table
                .username("sa")
                .password("qwe123==")
                .deserializer(new JsonDebeziumDeserializationSchema()) // converts SourceRecord to JSON String
                .build();
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        env.setStreamTimeCharacteristic(TimeCharacteristic.ProcessingTime);
        //2.Flink-CDC将读取binlog的位置信息以状态的方式保存在CK,如果想要做到断点续传,需要从Checkpoint或者Savepoint启动程序
        //2.1 开启Checkpoint,每隔5秒钟做一次CK
        env.enableCheckpointing(5000L);
        //2.2 指定CK的一致性语义
        env.getCheckpointConfig().setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE);
        //2.3 设置任务关闭的时候保留最后一次CK数据
       env.getCheckpointConfig().enableExternalizedCheckpoints(CheckpointConfig.ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION);
        //2.4 指定从CK自动重启策略
        env.setRestartStrategy(RestartStrategies.fixedDelayRestart(3, 2000L));
        //2.5 设置状态后端
        //env.setStateBackend(new RocksDBStateBackend("file:///usr/local/flink-1.13.5/ck"));

        env.setStateBackend(new MemoryStateBackend());

        // MemoryStateBackend（内存状态后端）
        // FsStateBackend（文件系统状态后端 hdfs）
        // RocksDBStateBackend（RocksDB状态后端）
        //env.setStateBackend(new FsStateBackend("hdfs://sc2:8020/flinkCDC"));
        //2.6 设置访问HDFS的用户名
        //System.setProperty("HADOOP_USER_NAME", "root");
        env.addSource(sourceFunction).addSink(new RedisSink()).setParallelism(1);
        //env.addSource(sourceFunction).setParallelism(1);
        env.execute("sqlServer2redis");
    }
}
