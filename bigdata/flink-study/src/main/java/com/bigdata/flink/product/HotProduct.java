package com.bigdata.flink.product;

import com.bigdata.flink.model.ProductViewCount;
import org.apache.commons.logging.Log;
import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.state.ListState;
import org.apache.flink.api.common.state.ListStateDescriptor;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.runtime.state.filesystem.FsStateBackend;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import com.bigdata.flink.model.UserBehavior;
import org.apache.flink.streaming.api.functions.AssignerWithPeriodicWatermarks;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.streaming.api.functions.windowing.WindowFunction;
import org.apache.flink.streaming.api.watermark.Watermark;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.security.Timestamp;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class HotProduct {
    /**
     * 热门商品统计
     */
    public static void main(String[] args) {


        Logger log = Logger
                .getLogger(HotProduct.class);

        log.setLevel(Level.ALL);


        //步骤一:获取执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        //步骤二:设置参数
        env.setParallelism(1);
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
        /*
        //设置检查点
        env.enableCheckpointing(60000);
        //设置检查方式 EXACTLY_ONCE
        env.getCheckpointConfig().setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE);
        //设置超时时间
        env.getCheckpointConfig().setCheckpointTimeout(120000);
        //设置检查点存储位置 hdfs
        env.setStateBackend(new FsStateBackend("hdfs://xxx"));
        */
        //步骤三:读取数据
        try {


            SingleOutputStreamOperator<String> p = env.readTextFile("D:\\javas\\bigdata\\data\\data1.csv")

                    .map(new MapFunction<String, UserBehavior>() {
                        //解析数据
                        @Override
                        public UserBehavior map(String s) throws Exception {
                            String[] fields = s.split(",");
                            UserBehavior userBehavior = new UserBehavior(Long.parseLong(fields[0]), Long.parseLong(fields[1]), Long.parseLong(fields[2]), fields[3], Long.parseLong(fields[4]), fields[5]);
                            return userBehavior;
                        }
                    }).assignTimestampsAndWatermarks(new EventTimeExtractor()) //指定watermark
                    .filter(userBehavior -> userBehavior.getBehavior().equals("P"))//过滤用户行为数据
                    .keyBy(userBehavior -> userBehavior.getProductId()) //按商品进行分组
                    .timeWindow(Time.hours(1), Time.minutes(5))//设置窗口
                    .aggregate(new CountProduct(), new WindowResult())////计算窗口数据
                    .keyBy(userBehavior -> userBehavior.getWindowEnd())//按照窗口进行分组
                    .process(new TopHotProduct()); //实现求TopN
                    p.print();

            //启动程序
            env.execute("HotProduct");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

class EventTimeExtractor implements AssignerWithPeriodicWatermarks<UserBehavior> {

    Long currentMaxEventTime = 0L;
    int maxOutOfOrderness = 10;

    @Nullable
    @Override
    //当前事件时间提前10秒
    public Watermark getCurrentWatermark() {
        return new Watermark((currentMaxEventTime - maxOutOfOrderness) * 1000);
    }

    //获取时间发生时间
    @Override
    public long extractTimestamp(UserBehavior userBehavior, long l) {
        Long timeStamp = userBehavior.getTimeStamp() * 1000;
        currentMaxEventTime = Math.max(timeStamp, currentMaxEventTime);
        return timeStamp;
    }

}

/**
 * IN 输入的数据类型
 * OUT输出的数据类型
 * ACC 辅助变量的数据类型
 */

class CountProduct implements AggregateFunction<UserBehavior, Long, Long> {

    @Override
    public Long createAccumulator() {
        return 0L;
    }

    @Override
    public Long add(UserBehavior userBehavior, Long aLong) {
        return aLong + 1;
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

class WindowResult implements WindowFunction<Long, ProductViewCount, Long, TimeWindow> {

    @Override
    public void apply(Long key, TimeWindow window, Iterable<Long> input, Collector<ProductViewCount> out) throws Exception {
        out.collect(new ProductViewCount(key, window.getEnd(), input.iterator().next()));
    }

}

/**
 * 里面实现求Topn逻辑
 * K,指定Key
 * I,输入的数据类型
 * O,输出的数据类型
 *
 * @param
 */
class TopHotProduct extends KeyedProcessFunction<Long, ProductViewCount, String> {
    private ListState<ProductViewCount> productState;

    @Override
    public void open(Configuration parameters) throws Exception {
        productState = getRuntimeContext().getListState(new ListStateDescriptor<ProductViewCount>("product-state", ProductViewCount.class));
    }

    @Override
    public void processElement(ProductViewCount productViewCount, Context context, Collector<String> collector) throws Exception {

        //把一个窗口里面的所有信息都存储下来
        productState.add(productViewCount);
        //注册一个定时器
        context.timerService().registerEventTimeTimer(productViewCount.getWindowEnd() + 1);
    }

    /**
     * 定时器里面实现排序的功能
     *
     * @param timestamp
     * @param ctx
     * @param
     */

    @Override
    public void onTimer(long timestamp, OnTimerContext ctx, Collector<String> out) throws Exception {

        Iterator it = productState.get().iterator();
        StringBuffer  sb =  new StringBuffer();
        List<ProductViewCount> list = new ArrayList<ProductViewCount>();
        while (it.hasNext()){
            ProductViewCount productViewCount = (ProductViewCount)it.next();
            list.add(productViewCount);
        }
        //根据count倒排
        List<ProductViewCount> sortList = list.stream().sorted(Comparator.comparing(ProductViewCount::getCount).reversed()).collect(Collectors.toList());
        List<ProductViewCount> sublist =  sortList.subList(0,3);

        productState.clear();
        //f = f.append(list.size()+"";
        sb.append("时间:").append(new Date(timestamp)).append("\n");
        for(ProductViewCount p:sublist){
            sb = sb.append("商品："+p.getProductId()+"   "+"数量："+p.getCount()+"  "+"时间："+new Date(p.getWindowEnd())+"\n");
        }

        out.collect(sb.toString());

    }


}
