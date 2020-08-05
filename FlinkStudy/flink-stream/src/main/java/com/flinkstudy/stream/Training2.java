package com.flinkstudy.stream;



import java.util.Arrays;
import java.util.List;

import org.apache.flink.api.common.JobExecutionResult;
import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.streaming.api.functions.sink.PrintSinkFunction;
import org.apache.flink.streaming.api.functions.source.FromElementsFunction;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.util.Collector;
import scala.Tuple3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

public class Training2 {
    private static List<Integer> data = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

    public static void main(String[] args) throws Exception {

        Tuple3 t3 = new Tuple3('a',"dfdfs",2);
        System.out.println(t3._2());

        int a = (Integer) t3._3();
        System.out.println(a+100);


        /*ExecutionEnvironment env = ExecutionEnvironment.createLocalEnvironment();

        DataSet<String> data = env.readTextFile("d://urlfile.txt");

        data.filter(new FilterFunction<String>() {
                    public boolean filter(String value) {
                        return value.startsWith("http://");
                    }
                })
                .writeAsText("d://result");

        JobExecutionResult res = env.execute();
        */
       // System.out.println(declarative());
//        System.out.println(imperative1());
//        System.out.println(imperative3());
//        System.out.println(imperative2());
//        dataStream();
//        state();
        //      processingTimeWindow();
    }

    //----------------------------------------

    /**
     * 1. Naive
     */
    public static int imperative1() {
        List<Integer> tempList = new ArrayList<>(10);
        for (int v : data) {
            tempList.add(v * 2);
        }
        int result = 0;
        for (int v : tempList) {
            result += v;
        }
        return result;
    }

    /**
     * 2. In-place
     */
    public static int imperative2() {
        for (int i = 0; i < data.size(); ++i) {
            data.set(i, data.get(i) * 2);
        }
        int result = 0;
        for (int v : data) {
            result += v;
        }
        return result;
    }

    /**
     * 3. Optimized
     */
    public static int imperative3() {
        int result = 0;
        for (int v : data) {
            result += v * 2;
        }
        return result;
    }

    /**
     * 4. Functional
     */
    public static int declarative() {


        return data.stream().mapToInt(v -> v * 2).sum();

    }

    //----------------------------------------------------------------------------------

    /**
     * 5. Basic DataStream API
     */
    public static void dataStream() throws Exception {
        StreamExecutionEnvironment e = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStream<Integer> source = e.addSource(
                new FromElementsFunction<>(Types.INT.createSerializer(e.getConfig()), data), Types.INT);
        DataStream<Integer> ds = source.map(v -> v * 2).keyBy(value -> 1).sum(0);
        ds.addSink(new PrintSinkFunction<>());
        System.out.println(e.getExecutionPlan());
        e.execute();
    }

    /**
     * 6. Sum with state.
     */
    public static void state() throws Exception {
        StreamExecutionEnvironment e = StreamExecutionEnvironment.getExecutionEnvironment();
        e.fromCollection(data)
                .keyBy(v -> v % 2)
                .process(new KeyedProcessFunction<Integer, Integer, Integer>() {
                    private ValueState<Integer> sumState;

                    @Override
                    public void open(Configuration parameters) throws Exception {
                        super.open(parameters);
                        ValueStateDescriptor<Integer> sumDescriptor = new ValueStateDescriptor<>(
                                "Sum",
                                Integer.class);
                        sumState = getRuntimeContext().getState(sumDescriptor);
                    }

                    @Override
                    public void processElement(Integer value, Context ctx, Collector<Integer> out) throws Exception {
                        Integer oldSum = sumState.value();
                        int sum = oldSum == null ? 0 : oldSum;
                        sum += value;
                        sumState.update(sum);
                        out.collect(sum);
                    }
                }).print().setParallelism(2);

        e.execute();
    }

    /**
     * 7. Processing time tumbling window.
     */
    public static void processingTimeWindow() throws Exception {
        StreamExecutionEnvironment e = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStreamSource<Integer> source = e
                .addSource(new SourceFunction<Integer>() {
                    private volatile boolean stop = false;

                    @Override
                    public void run(SourceContext<Integer> ctx) throws Exception {
                        int i = 0;
                        while (!stop && i < data.size()) {
                            ctx.collect(data.get(i++));
                            Thread.sleep(200);
                        }
                    }

                    @Override
                    public void cancel() {
                        stop = true;
                    }
                }).setParallelism(1);
        e.setStreamTimeCharacteristic(TimeCharacteristic.ProcessingTime); // optional for Processing time
        source.keyBy(v -> v % 2).process(new KeyedProcessFunction<Integer, Integer, Integer>() {

            @Override
            public void processElement(Integer integer, Context context, Collector<Integer> collector) throws Exception {

            }
        }).print().setParallelism(1);
        source.keyBy(v -> v % 2).process(new KeyedProcessFunction<Integer, Integer, Integer>() {
            private static final int WINDOW_SIZE = 400;
            private TreeMap<Long, Integer> windows;

            @Override
            public void open(Configuration parameters) throws Exception {
                super.open(parameters);
                windows = new TreeMap<>();
            }

            @Override
            public void processElement(Integer value, Context ctx, Collector<Integer> out) {
                long currentTime = ctx.timerService().currentProcessingTime();
                long windowStart = currentTime / WINDOW_SIZE;
                // Update the window
                int sum = windows.getOrDefault(windowStart, 0);
                windows.put(windowStart, sum + value);

                // Fire old windows
                Map<Long, Integer> oldWindows = windows.headMap(windowStart, false);
                Iterator<Map.Entry<Long, Integer>> iterator = oldWindows.entrySet().iterator();
                while (iterator.hasNext()) {
                    out.collect(iterator.next().getValue());
                    iterator.remove();
                }
            }

            @Override
            public void close() throws Exception {
                super.close();
                System.out.println(windows);
            }
        }).print().setParallelism(2);
        e.execute();
    }
}
