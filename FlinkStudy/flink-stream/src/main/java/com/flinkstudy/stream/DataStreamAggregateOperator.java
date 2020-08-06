package com.flinkstudy.stream;

import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.util.ArrayList;

public class DataStreamAggregateOperator {

    /**
     *
     * Aggregate 对KeyedStream按指定字段滚动聚合并输出每一次滚动聚合后的结果。默认的聚合函数有:sum、min、minBy、max、mabBy。
     *
     * 注意:
     *
     * max(field)与maxBy(field)的区别: maxBy返回field最大的那条数据;而max则是将最大的field的值赋值给第一条数据并返回第一条数据。同理,min与minBy。
     * Aggregate聚合算子会滚动输出每一次聚合后的结果。
     * Summary:
     *     Aggregate: min()、minBy()、max()、maxBy() 滚动聚合并输出每次滚动聚合后的结果
     */

    public static void main(String[] args) {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

// 输入: 用户行为。某个用户在某个时刻点击或浏览了某个商品，以及商品的价格。
        ArrayList<UserActionLogPOJO> userActionLogs = new ArrayList<>();
        UserActionLogPOJO userActionLog1 = new UserActionLogPOJO("userID1","productID3",10);
        userActionLogs.add(userActionLog1);

        UserActionLogPOJO userActionLog2 = new UserActionLogPOJO("userID2","",10);
        userActionLogs.add(userActionLog2);

        UserActionLogPOJO userActionLog3 = new UserActionLogPOJO("userID1","productID5",30);
        userActionLogs.add(userActionLog3);


        DataStreamSource<UserActionLogPOJO> source = env.fromCollection(userActionLogs);

        KeyedStream<UserActionLogPOJO,String> keyedStream = source.keyBy(new KeySelector<UserActionLogPOJO, String>() {
            @Override
            public String getKey(UserActionLogPOJO value) throws Exception {
                return value.getUserID();
            }
        });
        keyedStream.max("productPrice").print();

    }
}
