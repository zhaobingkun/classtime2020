package com.classtime.springcloud.entities;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;


import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class readJson {

    public static List<String> getJsonValue(String path){
        List<String> returnList = new ArrayList<String>();
        String s = readJsonFile(path);
        JSONArray jsonArray = JSONArray.parseArray(s);

        int size = jsonArray.size();
        for (int i = 0; i < size; i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            returnList.add(jsonObject.getString("name"));
            //System.out.println(i+"=="+jsonObject.getString("name"));
        }
        return returnList;
    }

    public static void main(String[] args) throws IOException {

        String path = "D:\\javas\\city.json";//readJson.class.getClassLoader().getResource("Movie.json").getPath();
        System.out.println(path);
        List<String> cityList = readJson.getJsonValue(path);
        for(int i=0;i<cityList.size();i++){
            System.out.println(i+"=="+cityList.get(i));
        }

        System.out.println("git");
        System.out.println("git");
        System.out.println("git");


/*



*/



        //Map<String, String> fieldMap = new HashMap<String, String>();
     /*   List<jsonData> jsonList = new ArrayList<jsonData>();
        int count = 0;
        for (int j = 1; j <= 59; j++) {
            String path = "E:\\公示数据20200611/p" + j + ".json";//readJson.class.getClassLoader().getResource("Movie.json").getPath();
            System.out.println(path);
            String s = readJsonFile(path);
            JSONObject jobj = JSON.parseObject(s);
            JSONArray movies = jobj.getJSONArray("rows");//构建JSONArray数组
            for (int i = 0; i < movies.size(); i++) {
                JSONObject key = (JSONObject) movies.get(i);
                jsonData objData = JSON.parseObject(key.toJSONString(), jsonData.class);
                jsonList.add(objData);

                //存sqlserver数据库

             *//*  try {
                    readJson.saveToDb(objData);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
               *//*
*//*                count++;
                System.out.print("count==" + count + "==" + objData.getMaterialname() + "==");
                System.out.println("");*//*
            }

        }*/


//写csv文件
/*

        File csv = new File("d:\\jsonData_tab_new.csv");//CSV文件
        BufferedWriter bw = new BufferedWriter(new FileWriter(csv, true));
        for (int i=0;i<jsonList.size();i++){
            System.out.println(i+"=="+jsonList.get(i).getMaterialname());
            bw.newLine();
            bw.write(jsonList.get(i).getProductcode() +"\t"
                        + jsonList.get(i).getMaterialname() + "\t"
                            + jsonList.get(i).getGoodscode() + "\t"
                            + jsonList.get(i).getGoodsname() + "\t"
                            + jsonList.get(i).getRegisteredoutlook() + "\t"
                            + jsonList.get(i).getCompanynamesc() + "\t"
                            + jsonList.get(i).getGoodsstandardcode() + "\t"
                            + jsonList.get(i).getMinunit() + "\t"
                            + jsonList.get(i).getProductmedicinemodel() + "\t"
                            + jsonList.get(i).getProductremark() + "\t"
                            + jsonList.get(i).getApprovalcode() + "\t"
                            + jsonList.get(i).getUnit() + "\t"
                            + jsonList.get(i).getRegisteredmedicinemodel() + "\t"
                            + jsonList.get(i).getRegisteredproductname() + "\t"
                            + jsonList.get(i).getProductname() + "\t"
                            + jsonList.get(i).getFactor() + "\t"
                    + jsonList.get(i).getProductinsurancetype()
            );
        }
        bw.close();
*/



        /*
                for (Map.Entry<String, Object> entry : key.entrySet()) {
                    String tmp = key.getString(entry.getKey());
                    System.out.print(entry.getKey()+"=="+tmp);
                   //fieldMap.put(entry.getKey(), entry.getKey());
                }*/
  /*
   取key值
   int i = 0;
        for (Map.Entry<String, String> entry : fieldMap.entrySet()) {
            i++;
            System.out.println(i + "==" + entry.getKey());
        }*/
    }


    public static void saveToDb(jsonData jsdata) throws SQLException {
        int num = 0;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            // 3. 建立连接
            conn = DBPoolHelper.getConnection();
            PreparedStatement ps = null;
            // 4. 操作数据
            String sql = "";

            String productcode = jsdata.getProductcode();
            String materialname = jsdata.getMaterialname();
            String goodscode = jsdata.getGoodscode();
            String goodsname = jsdata.getGoodsname();
            String registeredoutlook = jsdata.getRegisteredoutlook();
            String companynamesc = jsdata.getCompanynamesc();
            String goodsstandardcode = jsdata.getGoodsstandardcode();
            String minunit = jsdata.getMinunit();
            String productmedicinemodel = jsdata.getProductmedicinemodel();
            String productremark = jsdata.getProductremark();
            String approvalcode = jsdata.getApprovalcode();
            String unit = jsdata.getUnit();
            String registeredmedicinemodel = jsdata.getRegisteredmedicinemodel();
            String registeredproductname = jsdata.getRegisteredproductname();
            String productname = jsdata.getProductname();
            String factor = jsdata.getFactor();
            String productinsurancetype = jsdata.getProductinsurancetype();

            sql = "nsert into JsonData (productcode,materialname,goodscode,goodsname,registeredoutlook,companynamesc,goodsstandardcode,minunit,productmedicinemodel,productremark,approvalcode,unit,registeredmedicinemodel,registeredproductname,productname,factor,productinsurancetype) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            ps = conn.prepareStatement(sql);
            ps.setString(1, productcode);
            ps.setString(2, materialname);
            ps.setString(3, goodscode);
            ps.setString(4, goodsname);
            ps.setString(5, registeredoutlook);
            ps.setString(6, companynamesc);
            ps.setString(7, goodsstandardcode);
            ps.setString(8, minunit);
            ps.setString(9, productmedicinemodel);
            ps.setString(10, productremark);
            ps.setString(11, approvalcode);
            ps.setString(12, unit);
            ps.setString(13, registeredmedicinemodel);
            ps.setString(14, registeredproductname);
            ps.setString(15, productname);
            ps.setString(16, factor);
            ps.setString(17, productinsurancetype);
            int resultSet = ps.executeUpdate();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            conn.close();
        }
    }

    //读取json文件
    public static String readJsonFile(String fileName) {
        String jsonStr = "";
        try {
            File jsonFile = new File(fileName);
            FileReader fileReader = new FileReader(jsonFile);
            Reader reader = new InputStreamReader(new FileInputStream(jsonFile), "utf-8");
            int ch = 0;
            StringBuffer sb = new StringBuffer();
            while ((ch = reader.read()) != -1) {
                sb.append((char) ch);
            }
            fileReader.close();
            reader.close();
            jsonStr = sb.toString();
            return jsonStr;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
