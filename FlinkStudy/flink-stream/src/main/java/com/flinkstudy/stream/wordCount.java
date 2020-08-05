package com.flinkstudy.stream;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.FoldFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

import net.sf.json.JSONArray;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class wordCount {
    public static void main(String[] args) throws Exception {
/*
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();

        DataSet<String> text = env.readTextFile("d:\\wordcount.txt");

        DataSet<Tuple2<String, Integer>> counts =
                // split up the lines in pairs (2-tuples) containing: (word,1)
                text.flatMap(new Tokenizer())
                        // group by the tuple field "0" and sum up tuple field "1"
                        .groupBy(0)
                        .sum(1);
      counts.print();
*/
/*
        Stream<List<Integer>> inputStream = Stream.of(
                Arrays.asList(1),
                Arrays.asList(2, 3),
                Arrays.asList(4, 5, 6)
        );
        Stream<Integer> outputStream = inputStream.
                flatMap((childList) -> childList.stream());

        outputStream.forEach(p -> System.out.println(p.shortValue()));
*/

/*
        List<String> strs = Arrays.asList("好,好,学", "习,天,天", "向,上");

        List<String[]> strArray = strs.stream().map(str -> str.split(",")).collect(Collectors.toList());

        JSONArray jsonArray = JSONArray.fromObject(strArray);

        System.out.println("strArray => " + jsonArray.toString());

        // flatMap与map的区别在于 flatMap是将一个流中的每个值都转成一个个流，然后再将这些流扁平化成为一个流 。
        List<String> strList = strs.stream().map(str -> str.split(",")).flatMap(Arrays::stream)
                .collect(Collectors.toList());

        System.out.println(strList);
*/

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



        // 转换: Fold 基于初始值和FoldFunction滚动折叠
        SingleOutputStreamOperator<String> result = keyedStream.fold("浏览的商品及价格:", new FoldFunction<UserAction, String>() {
            @Override
            public String fold(String accumulator, UserAction value) throws Exception {
                if(accumulator.startsWith("userID")){
                    return accumulator + " -> " + value.getProductID()+":"+value.getProductPrice();
                }else {
                    return value.getUserID()+" " +accumulator + " -> " + value.getProductID()+":"+value.getProductPrice();
                }
            }
        });


/*        KeyedStream<UserAction,String> keyedStream1 = source.keyBy(new KeySelector<UserAction, String>() {
            @Override
            public String getKey(UserAction value) throws Exception {
                return value.getEventType();
            }
        });*/

 /*       SingleOutputStreamOperator<UserAction> result1 = keyedStream1.reduce(new ReduceFunction<UserAction>() {
            @Override
            public UserAction reduce(UserAction value1, UserAction value2) throws Exception {
                return value1.getProductPrice()+value2.getProductPrice();
            }
        });
*/




 /*       SingleOutputStreamOperator<String> result1 = keyedStream.fold("",new FoldFunction<UserAction,String>(){
            @Override
            public String fold(String accumulator, UserAction value) throws Exception {

                return null;
            }
        });*/


        // 输出: 输出到控制台
        // 每一条数据都会触发计算并输出
        // userID1 浏览的商品及价格: -> productID1:10
        // userID1 浏览的商品及价格: -> productID1:10 -> productID1:10
        // userID1 浏览的商品及价格: -> productID1:10 -> productID1:10 -> productID3:10
        // userID1 浏览的商品及价格: -> productID1:10 -> productID1:10 -> productID3:10 -> productID1:10
        // userID2 浏览的商品及价格: -> productID2:8
        // userID2 浏览的商品及价格: -> productID2:8 -> productID2:8
        // userID2 浏览的商品及价格: -> productID2:8 -> productID2:8 -> productID2:8
        result.print();

        env.execute();


        //forEach(p -> System.out.println(p.getName()));


        //counts.writeAsCsv("d:\\output\\", "\n", " ");
        //counts.writeAsCsv("d:\\output\\", "\n", " ");

        //args[0] = "d:\\wordcount.txt";
/*
        final MultipleParameterTool params = MultipleParameterTool.fromArgs(args);

        // set up the execution environment
        final ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();

        // make parameters available in the web interface
        env.getConfig().setGlobalJobParameters(params);

        // get input data
        DataSet<String> text = null;
        if (params.has("input")) {
            // union all the inputs from text files
            for (String input : params.getMultiParameterRequired("input")) {
                if (text == null) {
                    text = env.readTextFile(input);
                } else {
                    text = text.union(env.readTextFile(input));
                }
            }
            Preconditions.checkNotNull(text, "Input DataSet should not be null.");
        } else {
            // get default test text data
            System.out.println("Executing WordCount example with default input data set.");
            System.out.println("Use --input to specify file input.");
            text = WordCountData.getDefaultTextLineDataSet(env);
        }

        DataSet<Tuple2<String, Integer>> counts =
                // split up the lines in pairs (2-tuples) containing: (word,1)
                text.flatMap(new Tokenizer())
                        // group by the tuple field "0" and sum up tuple field "1"
                        .groupBy(0)
                        .sum(1);

        // emit result
        if (params.has("output")) {
            //counts.writeAsCsv(params.get("output"), "\n", " ");
            counts.writeAsCsv("d:\\output\\", "\n", " ");
            // execute program
            env.execute("WordCount Example");
       } else { System.out.println("Printing result to stdout. Use --output to specify output path.");
            counts.print();
        }*/


    }


    // User-defined functions
    public static class Tokenizer implements FlatMapFunction<String, Tuple2<String, Integer>> {

        @Override
        public void flatMap(String value, Collector<Tuple2<String, Integer>> out) {
            // normalize and split the line
            String[] tokens = value.toLowerCase().split(" ");

            // emit the pairs
            for (String token : tokens) {
                System.out.println(token);
                if (token.length() > 0) {
                    out.collect(new Tuple2<String, Integer>(token, 1));
                }
            }
        }
    }


}
