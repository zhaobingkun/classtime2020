package com.bigdata.flink.transform;

import com.bigdata.flink.source.MyNoParalleSource;
import org.apache.flink.streaming.api.collector.selector.OutputSelector;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SplitStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.util.ArrayList;

public class SplitDemo {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStreamSource<Long> text = env.addSource(new MyNoParalleSource()).setParallelism(1);


        //对流进行切分，按照数据的奇偶性进行区分

        SplitStream<Long> splits = text.split(new OutputSelector<Long>() {
            @Override
            public Iterable<String> select(Long aLong) {
                ArrayList<String> OutList = new ArrayList();
                if (aLong % 2 == 0) {
                    OutList.add("even");//偶数
                } else {
                    OutList.add("odd");//奇数
                }
                return OutList;
            }
        });
        //选择一个或者多个切分后的流
        DataStream<Long> even = splits.select("even");
        DataStream<Long> odd = splits.select("odd");
        DataStream<Long> all = splits.select("even","odd");
        odd.print();
        env.execute("split...");



    }
}
