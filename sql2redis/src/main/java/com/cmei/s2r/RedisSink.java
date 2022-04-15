package com.cmei.s2r;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;
import org.apache.flink.streaming.connectors.redis.common.mapper.RedisCommand;
import org.apache.flink.streaming.connectors.redis.common.mapper.RedisCommandDescription;
import org.apache.flink.streaming.connectors.redis.common.mapper.RedisMapper;
import redis.clients.jedis.Jedis;

import java.util.HashMap;

public class RedisSink extends RichSinkFunction<String> {

    private transient Jedis jedis;

    @Override
    public void open(Configuration config) {
        //ParameterTool parameters = (ParameterTool)getRuntimeContext().getExecutionConfig().getGlobalJobParameters();
        String host = "192.168.6.48";//parameters.getRequired("192.168.6.48");
        String password = "";// parameters.get("redis.password", "");
        Integer port = 6379;//parameters.getInt("redis.port", 6379);
        Integer timeout = 5000;//parameters.getInt("redis.timeout", 5000);
        Integer db = 0;//parameters.getInt("redis.db", 0);
        jedis = new Jedis(host, port, timeout);
        //jedis.auth(password);
        jedis.select(db);

    }


    @Override
    public void invoke(String value, Context context) throws Exception {

        if (!jedis.isConnected()) {
            jedis.connect();
        }

        //Preservation
        // jedis.set(value.f0,value.f1);
        Gson t = new Gson();
        HashMap<String, Object> hs = t.fromJson(value, HashMap.class);
        LinkedTreeMap<String, Object> source = (LinkedTreeMap<String, Object>) hs.get("source");

        System.out.println(" value = " + value);
        String database = (String) source.get("db");
        String table = (String) source.get("table");
        String op = (String) hs.get("op");
        System.out.println("database = " + database);
        System.out.println("table = " + table);
        System.out.println("op = " + op);


         // "op": "u",  修改
        // "op": "d", 删除
        //c、u、d、r，各自对应 create、update、delete、read。

        if ("inventory".equals(database) && "products".equals(table)) {
            if ("r".equals(op) || "c".equals(op) || "u".equals(op)) {
                System.out.println("insert => " + value);
                LinkedTreeMap<String, Object> data = (LinkedTreeMap<String, Object>) hs.get("after");
                Double ids = (Double) data.get("id");
                int id = ids.intValue();
                String name = (String) data.get("name");
                String description = (String) data.get("description");
                Double weights = (Double) data.get("weight");
                float weight = 0;
                if ("".equals(weights) || weights != null) {
                    weight = weights.floatValue();
                }
                jedis.set(id + "", name);
            }
            else if("d".equals(op)){
                LinkedTreeMap<String, Object> data = (LinkedTreeMap<String, Object>) hs.get("before");
                Double ids = (Double) data.get("id");
                int id = ids.intValue();
                jedis.del(id + "");
            }


        }
    }

    @Override
    public void close() throws Exception {
        System.out.println("closeclosecloseclosecloseclosecloseclosecloseclose");
        jedis.close();
    }

}
