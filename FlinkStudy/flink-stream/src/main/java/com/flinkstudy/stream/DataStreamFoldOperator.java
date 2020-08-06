package com.flinkstudy.stream;

import org.apache.flink.api.common.functions.FoldFunction;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.util.Arrays;

public class DataStreamFoldOperator {

    /**
     * 基于初始值和FoldFunction进行滚动折叠(Fold)，并向下游算子输出每次滚动折叠后的结果。
     * 注意: Fold会输出每一次滚动折叠的结果。
     * Summary:
     * Fold: 基于初始值和自定义的FoldFunction滚动折叠后发出新值
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


        // 转换: KeyBy对数据重分区
        KeyedStream<UserAction, String> keyedStream = source.keyBy(new KeySelector<UserAction, String>() {
            @Override
            public String getKey(UserAction value) throws Exception {
                return value.getUserID();
            }
        });




        keyedStream.max("ProductPrice").print();

        // 转换: Fold 基于初始值和FoldFunction滚动折叠
        SingleOutputStreamOperator<String> result = keyedStream.fold("", new FoldFunction<UserAction, String>() {
            @Override
            public String fold(String accumulator, UserAction value) throws Exception {
                if (accumulator.startsWith("userID")) {
                    return accumulator + " -> " + value.getProductID() + ":" + value.getProductPrice();
                } else {
                    return value.getUserID() + " ----" + accumulator + " -> " + value.getProductID() + ":" + value.getProductPrice();
                }
            }
        });

        result.print();
        env.execute();




    }
}
