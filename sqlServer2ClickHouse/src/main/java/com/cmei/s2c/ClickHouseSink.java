package com.cmei.s2c;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.HashMap;

public class ClickHouseSink extends RichSinkFunction<String> {

    Connection connection;

    PreparedStatement pstmt;
    PreparedStatement iStmt;
    PreparedStatement dStmt;
    PreparedStatement uStmt;
    private Connection getConnection() {
        Connection conn = null;
        try {
            Class.forName("ru.yandex.clickhouse.ClickHouseDriver");
            String url = "jdbc:clickhouse://192.168.10.61:8123/drugdb";
            conn = DriverManager.getConnection(url,"bigdata","bigdata");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }


    @Override
    public void open(Configuration parameters) throws Exception {
        super.open(parameters);
        connection = getConnection();
        String insertSql = "insert into product(id,name,description,weight) values (?,?,?,?)";
        String deleteSql = "delete from product where id=?";
        String updateSql = "update product set name=? ,description=?,weight=? where id=?";
        iStmt = connection.prepareStatement(insertSql);
        dStmt = connection.prepareStatement(deleteSql);
        uStmt = connection.prepareStatement(updateSql);

    }

    // 每条记录插入时调用一次
    public void invoke(String value, Context context) throws Exception {

        Gson t = new Gson();
        HashMap<String, Object> hs = t.fromJson(value, HashMap.class);

        LinkedTreeMap<String,Object> source = (LinkedTreeMap<String,Object>)hs.get("source");
        String database = (String) source.get("db");
        String table = (String) source.get("table");
        String op = (String) hs.get("op");
        /**
         * {"before":null,
         * "after":{"id":109,"name":"spare tire","description":"24 inch spare tire","weight":22.2},
         * "source":{"version":"1.5.4.Final","connector":"sqlserver","name":"sqlserver_transaction_log_source","ts_ms":1648776173094,"snapshot":"last","db":"inventory","sequence":null,"schema":"dbo","table":"products","change_lsn":null,"commit_lsn":"0000002c:00001a60:0001","event_serial_no":null},
         * "op":"r","ts_ms":1648776173094,"transaction":null}*/

        //实现insert方法
        if ("inventory".equals(database) && "products".equals(table)) {
            if ("r".equals(op) || "c".equals(op)) {
                LinkedTreeMap<String, Object> data = (LinkedTreeMap<String, Object>) hs.get("after");
                Double ids = (Double)data.get("id");
                int id =  ids.intValue();
                String name = (String) data.get("name");
                String description = (String) data.get("description");
                Double weights = (Double)data.get("weight");
                float weight=0;
                if("".equals(weights) || weights != null ){
                    weight =  weights.floatValue();
                }

                iStmt.setInt(1, id);
                iStmt.setString(2, name);
                iStmt.setString(3, description);
                iStmt.setFloat(4, weight);

                iStmt.executeUpdate();
            }

//            else if ("d".equals(type)) {
//                System.out.println("delete => " + value);
//                LinkedTreeMap<String, Object> data = (LinkedTreeMap<String, Object>) hs.get("data");
//                String id = (String) data.get("ID");
//                dStmt.setString(1, id);
//                dStmt.executeUpdate();
//            }
//            else if ("u".equals(type)) {
//                System.out.println("update => " + value);
//                LinkedTreeMap<String, Object> data = (LinkedTreeMap<String, Object>) hs.get("data");
//                String id = (String) data.get("ID");
//                String cron = (String) data.get("CRON");
//                uStmt.setString(1, cron);
//                uStmt.setString(2, id);
//                uStmt.executeUpdate();
//            }
        }
    }

    @Override
    public void close() throws Exception {
        super.close();

        if(pstmt != null) {
            pstmt.close();
        }

        if(connection != null) {
            connection.close();
        }
    }

}
