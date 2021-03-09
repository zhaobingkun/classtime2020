package com.bigdata.flink.transform;

import com.bigdata.flink.source.MyNoParalleSource;
import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;


/**
 * 数据源：1 2 3 4 5.....源源不断过来
 * 通过map打印一下接受到数据
 * 通过filter过滤一下数据，我们只需要偶数
 */

public class MapDemo {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStreamSource<Long> dataStreamSource = env.addSource(new MyNoParalleSource());
        SingleOutputStreamOperator<Long> map = dataStreamSource.map(new MapFunction<Long, Long>() {
            @Override
            public Long map(Long value) throws Exception {
                return value;
            }
        });

        SingleOutputStreamOperator<Long> filter = map.filter(new FilterFunction<Long>() {
            @Override
            public boolean filter(Long aLong) throws Exception {
                return aLong % 2 == 0;
            }
        });

        filter.print().setParallelism(1);
        env.execute("filter ....");


    }
}
