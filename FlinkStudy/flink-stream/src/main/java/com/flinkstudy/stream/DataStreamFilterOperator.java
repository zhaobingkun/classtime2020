package com.flinkstudy.stream;

import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.util.Arrays;

public class DataStreamFilterOperator {

    /**
     * filter是进行筛选。
     * Summary:
     *      Fliter: 过滤出需要的数据
     */

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        // 输入: 用户行为。某个用户在某个时刻点击或浏览了某个商品，以及商品的价格。
        DataStreamSource<UserAction> source = env.fromCollection(Arrays.asList(
                new UserAction("userID1", 1293984000, "click", "productID1", 10),
                new UserAction("userID2", 1293984001, "browse", "productID2", 8),
                new UserAction("userID1", 1293984002, "browse", "productID1", 10)
        ));

        SingleOutputStreamOperator<UserAction> result = source.filter(new FilterFunction<UserAction>() {
            @Override
            public boolean filter(UserAction value) throws Exception {

                //return value.getUserID().equals("userID1");
                return value.getEventType().equals("browse");
            }
        });
        result.print();
        SingleOutputStreamOperator<UserAction> result1 = result.map(new MapFunction<UserAction, UserAction>() {
            @Override
            public UserAction map(UserAction value) throws Exception {
                int price = value.getProductPrice()*10;
                return new UserAction(value.getUserID(),value.getEventTime(),value.getEventType(),value.getProductID(),price);
            }
        });
        result1.print();
        env.execute();

    }
}
