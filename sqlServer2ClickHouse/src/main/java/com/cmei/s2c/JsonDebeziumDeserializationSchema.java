package com.cmei.s2c;

import com.ververica.cdc.debezium.DebeziumDeserializationSchema;
import io.debezium.data.Envelope;
import org.apache.flink.api.common.typeinfo.BasicTypeInfo;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.util.Collector;
import org.apache.kafka.common.protocol.types.Struct;
import org.apache.kafka.connect.source.SourceRecord;


import java.util.HashMap;

public class JsonDebeziumDeserializationSchema implements DebeziumDeserializationSchema {
    @Override
    public void deserialize(SourceRecord sourceRecord, Collector collector) throws Exception {
        HashMap<String, Object> hashMap = new HashMap<>();

        String topic = sourceRecord.topic();
        String[] split = topic.split("[.]");
        String database = split[1];
        String table = split[2];
        hashMap.put("database",database);
        hashMap.put("table",table);

        //获取操作类型
        Envelope.Operation operation = Envelope.operationFor(sourceRecord);
        //获取数据本身
        Struct struct = (Struct)sourceRecord.value();
        Struct after = struct.getStruct("after");
        Struct before = struct.getStruct("before");
        /*
         	 1，同时存在 beforeStruct 跟 afterStruct数据的话，就代表是update的数据
             2,只存在 beforeStruct 就是delete数据
             3，只存在 afterStruct数据 就是insert数据
        */
    }

    @Override
    public TypeInformation<String> getProducedType() {
        return  BasicTypeInfo.STRING_TYPE_INFO;
    }
}
