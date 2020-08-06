package com.flinkstudy.stream;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.util.Arrays;

public class DataStreamMapOperator {
    /**
     * map可以理解为映射，对每个元素进行一定的变换后，映射为另一个元素。
     * Summary:
     *      Map: 一对一转换
     */
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        // 输入: 用户行为。某个用户在某个时刻点击或浏览了某个商品，以及商品的价格。
        DataStreamSource<UserAction> source = env.fromCollection(Arrays.asList(
                new UserAction("userID1", 1293984000, "click", "productID1", 10),
                new UserAction("userID2", 1293984001, "browse", "productID2", 8),
                new UserAction("userID1", 1293984002, "click", "productID1", 10)
        ));

        SingleOutputStreamOperator<UserAction> result = source.map(new MapFunction<UserAction,UserAction>(){

            @Override
            public UserAction map(UserAction value) throws Exception {
                int price = value.getProductPrice()*8;
                return new UserAction(value.getUserID(),value.getEventTime(),value.getEventType(),value.getProductID(),price);
            }
        });
        result.print();
        env.execute();


    }
}
