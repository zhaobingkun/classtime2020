package com.bigdata.flink.source;

import org.apache.flink.streaming.api.functions.source.SourceFunction;

import javax.xml.transform.Source;


/**
 *
 * 我们数据输出的数据类型
 *
 * 代表我们的这个数据源只能支持一个并行度（单并行度）
 */
public class MyNoParalleSource implements SourceFunction<Long> {
    private long number = 1L;
    private boolean isRunning = true;
    @Override
    public void run(SourceContext<Long> sourceContext) throws Exception {
        while (isRunning){
            sourceContext.collect(number);
            number++;
            Thread.sleep(1000);
        }
    }

    @Override
    public void cancel() {

    }
}
