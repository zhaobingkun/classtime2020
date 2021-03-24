package com.bigdata.flink.product;

import com.bigdata.flink.model.CategoryViewCount;
import com.bigdata.flink.model.ProductViewCount;
import com.bigdata.flink.model.UserBehavior;
import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.api.common.state.ListState;
import org.apache.flink.api.common.state.ListStateDescriptor;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.runtime.state.filesystem.FsStateBackend;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.AssignerWithPunctuatedWatermarks;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.streaming.api.functions.windowing.WindowFunction;
import org.apache.flink.streaming.api.watermark.Watermark;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;
import org.apache.hadoop.io.NullWritable;

import javax.annotation.Nullable;
import java.security.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

public class HotCategory {
    /**
     * 热门商品统计
     */
    public static void main(String[] args) throws Exception {
        //步骤一:获取执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        //步骤二:设置参数
        env.setParallelism(1).setStreamTimeCharacteristic(TimeCharacteristic.EventTime);

        //设置检查点
        //env.enableCheckpointing(6000);

        //设置检查方式 EXACTLY_ONCE
        //env.getCheckpointConfig().setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE);
        //设置超时时间
        //env.getCheckpointConfig().setCheckpointTimeout(12000);
        //设置检查点存储位置 hdfs
        //env.setStateBackend(new FsStateBackend("hdfs://xxx"));
        //步骤三:读取数据
        env.readTextFile("D:\\javas\\bigdata\\data\\data1.csv")
        .map(s->{
            String[] fields = s.split(",");
            return new UserBehavior(Long.parseLong(fields[0]), Long.parseLong(fields[1]), Long.parseLong(fields[2]), fields[3], Long.parseLong(fields[4]), fields[5]);
        })
        .assignTimestampsAndWatermarks(new EventTimeExtractorByCategory())//设置水线
        .filter(userBehavior -> userBehavior.getBehavior().equals("P"))
                .keyBy(userBehavior -> userBehavior.getCategoryId())
                .timeWindow(Time.hours(1),Time.minutes(5))
                .aggregate(new CountCategory(),new WindowResultByCategory())
                .keyBy(userBehavior->userBehavior.getWindowEnd())
                .process(new TopHotCategory())
                .print();

        //步骤四:启动程序
        env.execute("HotCategory");
    }

}
class TopHotCategory extends KeyedProcessFunction<Long,CategoryViewCount,String> {

    private ListState<CategoryViewCount> categoryState;


    @Override
    public void open(Configuration parameters) throws Exception {
        categoryState = getRuntimeContext().getListState(new ListStateDescriptor<CategoryViewCount>("category",CategoryViewCount.class));
    }
    @Override
    public void processElement(CategoryViewCount categoryViewCount, Context context, Collector<String> collector) throws Exception {
        categoryState.add(categoryViewCount);
        context.timerService().registerEventTimeTimer(categoryViewCount.getWindowEnd() + 1);
    }

    @Override
    public void onTimer(long timestamp, OnTimerContext ctx, Collector<String> out) throws Exception {
        Iterator<CategoryViewCount> it = categoryState.get().iterator();
        StringBuffer  sb =  new StringBuffer();
        List<CategoryViewCount> list = new ArrayList<CategoryViewCount>();
        while(it.hasNext()){
            list.add(it.next());
        }
        List<CategoryViewCount>  sortList = list.stream().sorted(Comparator.comparing(CategoryViewCount::getCount).reversed()).collect(Collectors.toList());
            int topN=0;
        if(sortList.size()>3){
            topN=3;
        }
        else{
            topN=sortList.size();
        }
        List<CategoryViewCount> sublist =  sortList.subList(0,topN);
        categoryState.clear();
        sb.append("时间："+new Date(timestamp)).append("\n");
        for(CategoryViewCount cc : sublist){
            sb.append("类别："+cc.getCategoryId()+"   数量:"+cc.getCount()+"\n");
        }
        out.collect(sb.toString());


    }

}

class WindowResultByCategory implements WindowFunction<Long, CategoryViewCount,Long, TimeWindow>{

    @Override
    public void apply(Long aLong, TimeWindow timeWindow, Iterable<Long> iterable, Collector<CategoryViewCount> out) throws Exception {
        out.collect(new CategoryViewCount(aLong,timeWindow.getEnd(),iterable.iterator().next()));
    }
}

class CountCategory implements AggregateFunction<UserBehavior,Long,Long>{

    @Override
    public Long createAccumulator() {
        return 0L;
    }

    @Override
    public Long add(UserBehavior userBehavior, Long aLong) {
        return aLong+1;
    }

    @Override
    public Long getResult(Long aLong) {
        return aLong;
    }
    @Override
    public Long merge(Long aLong, Long acc1) {
        return aLong + acc1;
    }
}




 class EventTimeExtractorByCategory implements AssignerWithPunctuatedWatermarks<UserBehavior>{

    Long currentMaxEventTime = 0L;
    Long maxOutOfOrderness = 10L;

    @Nullable
    @Override
    public Watermark checkAndGetNextWatermark(UserBehavior userBehavior, long l) {
        return new Watermark((currentMaxEventTime-maxOutOfOrderness) * 1000);
    }

    @Override
    public long extractTimestamp(UserBehavior userBehavior, long l) {
        Long timeStamp = userBehavior.getTimeStamp() * 1000;
        currentMaxEventTime =  Math.max(currentMaxEventTime,maxOutOfOrderness);
        return timeStamp;
    }
}
