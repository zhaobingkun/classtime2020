package com.flinkstudy.stream;

import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import javax.swing.*;
import javax.xml.crypto.*;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import java.util.Arrays;

public class DataStreamKeyByOperator {

    /**
     *
     * KeyBy: 按指定的Key对数据重分区。将同一Key的数据放到同一个分区。
     *
     * 注意:
     *
     * 分区结果和KeyBy下游算子的并行度强相关。如下游算子只有一个并行度,不管怎么分，都会分到一起。
     * 对于POJO类型，KeyBy可以通过keyBy(fieldName)指定字段进行分区。
     * 对于Tuple类型，KeyBy可以通过keyBy(fieldPosition)指定字段进行分区。
     * 对于一般类型，如上, KeyBy可以通过keyBy(new KeySelector {…})指定字段进行分区。
     *
     * Summary:
     *      KeyBy: 按指定的Key对数据重分区。将同一Key的数据放到同一个分区。
     */

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        // 输入: 用户行为。某个用户在某个时刻点击或浏览了某个商品，以及商品的价格。
        
        DataStreamSource<UserAction> source = env.fromCollection(Arrays.asList(
                new UserAction("userID1", 1293984000, "click", "productID1", 10),
                new UserAction("userID2", 1293984001, "browse", "productID2", 8),
                new UserAction("userID1", 1293984002, "browse", "productID1", 10)
        ));
        KeyedStream<UserAction,String> result = source.keyBy(new KeySelector<UserAction, String>() {
            @Override
            public String getKey(UserAction value) throws Exception {
                return value.getEventType();
            }
        });
        result.print().setParallelism(3);
        env.execute();
    }
}
