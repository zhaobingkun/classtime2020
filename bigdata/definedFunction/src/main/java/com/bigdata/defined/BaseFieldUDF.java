package com.bigdata.defined;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.hive.ql.exec.UDFMethodResolver;
import org.json.JSONException;
import org.json.JSONObject;

public class BaseFieldUDF extends UDF {
    public static void main(String[] args) {

        String line = "1598693683508|{\"cm\":{\"ln\":\"-108.1\",\"sv\":\"V2.7.4\",\"os\":\"8.0.4\",\"g\":\"6G07BL4X@gmail.com\",\"mid\":\"3\",\"nw\":\"4G\",\"l\":\"pt\",\"vc\":\"11\",\"hw\":\"750*1134\",\"ar\":\"MX\",\"uid\":\"3\",\"t\":\"1598627949192\",\"la\":\"5.0\",\"md\":\"sumsung-0\",\"vn\":\"1.0.0\",\"ba\":\"Sumsung\",\"sr\":\"X\"},\"ap\":\"app\",\"et\":[{\"ett\":\"1598647856166\",\"en\":\"newsdetail\",\"kv\":{\"entry\":\"2\",\"goodsid\":\"0\",\"news_staytime\":\"16\",\"loading_time\":\"9\",\"action\":\"3\",\"showtype\":\"1\",\"category\":\"73\",\"type1\":\"201\"}},{\"ett\":\"1598654400505\",\"en\":\"loading\",\"kv\":{\"extend2\":\"\",\"loading_time\":\"12\",\"action\":\"1\",\"extend1\":\"\",\"type\":\"2\",\"type1\":\"\",\"loading_way\":\"1\"}},{\"ett\":\"1598603902380\",\"en\":\"active_background\",\"kv\":{\"active_source\":\"3\"}},{\"ett\":\"1598632379633\",\"en\":\"error\",\"kv\":{\"errorDetail\":\"java.lang.NullPointerException\\\\n    at cn.lift.appIn.web.AbstractBaseController.validInbound(AbstractBaseController.java:72)\\\\n at cn.lift.dfdf.web.AbstractBaseController.validInbound\",\"errorBrief\":\"at cn.lift.dfdf.web.AbstractBaseController.validInbound(AbstractBaseController.java:72)\"}},{\"ett\":\"1598686251553\",\"en\":\"comment\",\"kv\":{\"p_comment_id\":1,\"addtime\":\"1598693035607\",\"praise_count\":283,\"other_id\":9,\"comment_id\":4,\"reply_count\":8,\"userid\":7,\"content\":\"焉蔫分偏挽拄久蝇寝堤蛇脸钉潘医都\"}}]}";
        String x = new BaseFieldUDF().evaluate(line, "mid,uid,vc,vn,l,sr,os,ar,md,ba,sv,g,hw,nw,ln,la,t");
        System.out.println(x);
    }


    public String evaluate(String line, String jsonkeysString) {
        // 0 准备一个builder
        StringBuilder builder = new StringBuilder();

        // 1 切割jsonkeys  mid uid vc vn l sr os ar md
        String[] jsonkeys = jsonkeysString.split(",");

        // 2 处理line   服务器时间 | json
        String[] logContents = line.split("\\|");

        // 3 合法性校验
        if (logContents.length != 2 || StringUtils.isBlank(logContents[1])) {
            return "";
        }

        try {
            JSONObject jsonObject = new JSONObject(logContents[1]);
            // 获取cm里面的对象
            JSONObject base = jsonObject.getJSONObject("cm");


            // 循环遍历取值
            for (int i = 0; i < jsonkeys.length; i++) {
                String filedName = jsonkeys[i].trim();

                if (base.has(filedName)) {
                    builder.append(base.getString(filedName)).append("\t");
                } else {
                    builder.append("\t");
                }
            }
            builder.append(jsonObject.getString("et")).append("\t");
            builder.append(logContents[0]).append("\t");




        } catch (JSONException e) {
            e.printStackTrace();
        }

        return builder.toString();
    }
}
