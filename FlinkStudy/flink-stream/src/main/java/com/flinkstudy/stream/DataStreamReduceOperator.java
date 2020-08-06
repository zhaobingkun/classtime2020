package com.flinkstudy.stream;

import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.util.Arrays;

public class DataStreamReduceOperator {
    /**
     * reduce是归并操作，它可以将KeyedStream 转变为 DataStream。
     *
     * Reduce: 基于ReduceFunction进行滚动聚合，并向下游算子输出每次滚动聚合后的结果。
     * 注意: Reduce会输出每一次滚动聚合的结果。
     * Summary:
     *     Reduce: 基于ReduceFunction进行滚动聚合，并向下游算子输出每次滚动聚合后的结果。
     */

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        // 输入: 用户行为。某个用户在某个时刻点击或浏览了某个商品，以及商品的价格。
        DataStreamSource<UserAction> source = env.fromCollection(Arrays.asList(
                new UserAction("userID1", 1293984000, "click", "productID1", 10),
                new UserAction("userID2", 1293984001, "browse", "productID2", 8),
                new UserAction("userID2", 1293984002, "browse", "productID2", 8),
                new UserAction("userID2", 1293984003, "browse", "productID2", 8),
                new UserAction("userID1", 1293984002, "click", "productID1", 10),
                new UserAction("userID1", 1293984003, "click", "productID3", 10),
                new UserAction("userID1", 1293984004, "click", "productID1", 10)
        ));


        /*
        // 转换: KeyBy对数据重分区
        KeyedStream<UserAction,String>  keyedStream = source.keyBy(new KeySelector<UserAction,String>(){
            @Override
            public String getKey(UserAction value) throws Exception {
                return value.getUserID();
            }
        });

        // 转换: Reduce滚动聚合。这里,滚动聚合每个用户对应的商品总价格。
        SingleOutputStreamOperator<UserAction> result = keyedStream.reduce(new ReduceFunction<UserAction>() {
            @Override
            public UserAction reduce(UserAction value1, UserAction value2) throws Exception {
                int newPrice = value1.getProductPrice()+value2.getProductPrice();
                return new UserAction(value1.getUserID(),-1,"","",newPrice);
            }
        });
        result.print();
        env.execute();*/

        KeyedStream<UserAction,String> keyedStream = source.keyBy(new KeySelector<UserAction, String>() {
            @Override
            public String getKey(UserAction value) throws Exception {
                return value.getProductID();
            }
        });

       SingleOutputStreamOperator<UserAction> result = keyedStream.reduce(new ReduceFunction<UserAction>() {
           @Override
           public UserAction reduce(UserAction value1, UserAction value2) throws Exception {
               int newPrice = value1.getProductPrice()+value2.getProductPrice();
               return new UserAction("value1.getUserID()",-1,"",value1.getProductID(),newPrice);
           }
       });
       result.print();
       env.execute();

    }
}
