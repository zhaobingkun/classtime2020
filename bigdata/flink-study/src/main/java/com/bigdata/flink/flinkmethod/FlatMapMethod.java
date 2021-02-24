package com.bigdata.flink.flinkmethod;

import com.bigdata.flink.model.WordCountModel;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.util.Collector;

public class FlatMapMethod implements FlatMapFunction<String, WordCountModel> {
    @Override
    public void flatMap(String s, Collector<WordCountModel> out) throws Exception {
        String[] fields = s.split(",");
        for(String word:fields){
            out.collect(new WordCountModel(word,1));
        }

    }
}
