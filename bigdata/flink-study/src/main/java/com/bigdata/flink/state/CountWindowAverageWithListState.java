package com.bigdata.flink.state;

import com.google.common.collect.Lists;
import org.apache.flink.api.common.functions.RichFlatMapFunction;
import org.apache.flink.api.common.functions.RuntimeContext;
import org.apache.flink.api.common.state.ListState;
import org.apache.flink.api.common.state.ListStateDescriptor;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.util.Collector;

import java.util.ArrayList;
import java.util.Collections;

public class CountWindowAverageWithListState extends RichFlatMapFunction<Tuple2<Long,Long>,Tuple2<Long,Double>> {

    private ListState<Tuple2<Long,Long>> elementsByKey;

    @Override
    public void open(Configuration parameters) throws Exception {

        ListStateDescriptor<Tuple2<Long,Long>>    average = new ListStateDescriptor<>(
                "average", Types.TUPLE(Types.TUPLE(Types.LONG,Types.LONG))
        );
        elementsByKey = getRuntimeContext().getListState(average);
    }


    @Override
    public void flatMap(Tuple2<Long, Long> element, Collector<Tuple2<Long, Double>> out) throws Exception {
        Iterable<Tuple2<Long,Long>> currentState = elementsByKey.get();
        if(currentState==null){
            elementsByKey.addAll(Collections.emptyList());
        }
        elementsByKey.add(element);

        ArrayList<Tuple2<Long,Long>>  allElements = Lists.newArrayList(elementsByKey.get());
        if (allElements.size()==3){
            long count = 0;
            long sum = 0;
            for(Tuple2<Long,Long> e:allElements){
                count++;
                sum += e.f1;
            }
            double avg = (double)sum/count;
            out.collect(Tuple2.of(element.f0,avg));
        }


    }
}
