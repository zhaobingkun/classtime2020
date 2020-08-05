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
