package com.bigdata.flink.state;

import akka.remote.transport.ProtocolStateActor;
import javassist.bytecode.analysis.Type;
import org.apache.flink.api.common.functions.RichFlatMapFunction;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.util.Collector;

public class CountWindowAverageWithValueState extends RichFlatMapFunction<Tuple2<Long, Long>, Tuple2<Long, Double>> {

    private ValueState<Tuple2<Long,Long>> countAndSum;


    @Override
    public void open(Configuration parameters) throws Exception {
        ValueStateDescriptor<Tuple2<Long,Long>>  average = new ValueStateDescriptor<>(
              "average"  , Types.TUPLE(Types.LONG,Types.LONG)
        );
        countAndSum = getRuntimeContext().getState(average);
    }

    @Override
    public void flatMap(Tuple2<Long, Long> element, Collector<Tuple2<Long, Double>> out) throws Exception {
        Tuple2<Long,Long> currentState = countAndSum.value();
        if (currentState==null){
            currentState = Tuple2.of(0L,0L);
        }
        //统计key出现的次数
        currentState.f0 += 1;
        currentState.f1 = currentState.f1 + element.f1;
        countAndSum.update(currentState);

        if(currentState.f0 == 3){
            double avg  = (double)currentState.f1/currentState.f0;
            out.collect(Tuple2.of(element.f0,avg));
            countAndSum.clear();
        }


    }

}
