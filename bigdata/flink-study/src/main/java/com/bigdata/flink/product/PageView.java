package com.bigdata.flink.product;

import com.bigdata.flink.model.UserBehavior;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.runtime.state.filesystem.FsStateBackend;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.AssignerWithPunctuatedWatermarks;
import org.apache.flink.streaming.api.watermark.Watermark;
import org.apache.flink.streaming.api.windowing.time.Time;

import javax.annotation.Nullable;

public class PageView {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        env.setParallelism(1).setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
        /*
        env.enableCheckpointing(60000);
        env.getCheckpointConfig().setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE);
        env.getCheckpointConfig().setCheckpointTimeout(12000);
        env.setStateBackend(new FsStateBackend("hdfs://xxx"));
*/
        env.readTextFile("D:\\javas\\bigdata\\data\\data1.csv")
                .map(s -> {
                    String[] fields = s.split(",");
                    UserBehavior userBehavior = new UserBehavior(Long.parseLong(fields[0]), Long.parseLong(fields[1]), Long.parseLong(fields[2]), fields[3], Long.parseLong(fields[4]), fields[5]);
                    return userBehavior;
                })
                .assignTimestampsAndWatermarks(new PageViewEventTimeExtractor())
                .filter(userBehavior -> userBehavior.getBehavior().equals("P"))
                .map(new MapFunction<UserBehavior, Tuple2<String,Integer>>() {
                    @Override
                    public Tuple2<String, Integer> map(UserBehavior userBehavior) throws Exception {
                        return new Tuple2<String, Integer>("P",1);
                    }
                }).timeWindowAll(Time.hours(1))

                .sum(1)
                .print();


        env.execute("Page View");

    }
}

class PageViewEventTimeExtractor implements AssignerWithPunctuatedWatermarks<UserBehavior> {
    Long currentMaxEventTime = 0L; //设置当前窗口里面最大的时间
    Integer maxOutofOrderness = 10;//最大乱序时间

    @Nullable
    @Override
    //计算watermark
    public Watermark checkAndGetNextWatermark(UserBehavior userBehavior, long l) {
        return new Watermark((currentMaxEventTime - maxOutofOrderness) * 1000);
    }

    @Override
    //指定我们的时间字段
    public long extractTimestamp(UserBehavior userBehavior, long l) {
        //时间字段
        Long timestap = userBehavior.getTimeStamp() * 1000;
        currentMaxEventTime = Math.max(userBehavior.getTimeStamp(), currentMaxEventTime);
        return timestap;
    }
}
