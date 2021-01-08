package com.bigdata.defined;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.hive.ql.exec.UDFArgumentException;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDTF;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspectorFactory;
import org.apache.hadoop.hive.serde2.objectinspector.StructObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorFactory;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;


/**
 * 自定义UDTF函数，需要继承GenericUDTF：重写initialize()；process(); close();
 */
public class EventJsonUDTF extends GenericUDTF {

    //该方法中，将指定输出参数的名称和参数类型：
    public StructObjectInspector initialize(ObjectInspector[] argOIs) throws UDFArgumentException {
        ArrayList<String> fieldNames = new ArrayList<String>();
        ArrayList<ObjectInspector> fieldOIs = new ArrayList<ObjectInspector>();
        fieldNames.add("event_name");
        fieldOIs.add(PrimitiveObjectInspectorFactory.javaStringObjectInspector);
        fieldNames.add("event_json");
        fieldOIs.add(PrimitiveObjectInspectorFactory.javaStringObjectInspector);

        return ObjectInspectorFactory.getStandardStructObjectInspector(fieldNames, fieldOIs);


    }


    //输入一条记录，输出若干条结果
    @Override
    public void process(Object[] objects) throws HiveException {
        // 获取传入的et
        String input = objects[0].toString();
        // 如果传进来的数据为空，直接返回过滤掉该数据
        if (StringUtils.isBlank(input)) {
            return;
        } else {
            try {
                // 获取共计的事件（ad/facoriters）
                JSONArray ja = new JSONArray(input);
                if (ja == null)
                    return;
                // 循环遍历每个事件
                for (int i = 0; i < ja.length(); i++) {
                    String[] result = new String[2];

                    try {
                        // 取出每一个的事件名称（ad/facoriters）
                        result[0] = ja.getJSONObject(i).getString("en");

                        // 取出每个事件整体
                        result[1] = ja.getString(i);
                    } catch (JSONException e) {
                        continue;
                    }

                    // 结果返回
                    forward(result);
                }



            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }


    //没有记录处理的时候该方法会被调用，清理使用
    @Override
    public void close() throws HiveException {

    }
}
