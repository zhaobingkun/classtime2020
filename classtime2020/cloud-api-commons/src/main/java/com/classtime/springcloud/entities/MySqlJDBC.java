package com.classtime.springcloud.entities;

import java.sql.*;
import java.util.*;

public class MySqlJDBC {

    private String dbDriver = "com.mysql.cj.jdbc.Driver";
    private String dbUrl = "jdbc:mysql://test.db.insurance.stbx:3306/spider?useUnicode=true&characterEncoding=utf8";
    private String dbUser = "root";
    private String dbPwd = "Pti9mPK3#BDQWrU!RL";

    private Connection conn;
    private Statement stmt;
    private ResultSet rs;


    public Connection getConn() {

        Connection conn = null;

        try {
            Class.forName(dbDriver);
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            conn = (Connection) DriverManager.getConnection(dbUrl, dbUser, dbPwd);

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return conn;

    }
    public static void main(String[] args) throws SQLException {
        //MySqlJDBC db = new MySqlJDBC();
        //Connection conn = db.getConn();
        /*
        Connection conn = DBPoolHelper.getConnection();
        String sql = "select * from tbnews;";
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            int id = rs.getInt("id");
            String name = rs.getString("name");
            String content = rs.getString("content");
            System.out.println(id + " : " + name + " : " + content);
            System.out.println();
        }
        conn.close();*/

        List<Integer> nums = new ArrayList<>();

        for (int i=0;i<=37;i++){
            nums.add(i);
            System.out.print(nums.get(i)+",");
        }
/*        for (int i=0;i<37;i++){
            System.out.println(nums.get(i));
        }*/
        System.out.println();
        //System.out.println("size = " + nums.size());

        Random random = new Random () ;
        for(int i =0;i <7;i++) {
            int numRandom = random.nextInt();
            int numRandom36 = numRandom % (nums.size()-1);
            int tmp =  1 + Math.abs(numRandom36);
            //System.out.print("tmp = " + tmp + ',') ;
            System.out.println("code = " + nums.get(tmp) + ",");
            nums.remove(tmp);
            System.out.println(nums);
        }
        /*
        for (int i=0;i<nums.size()-1;i++){
            System.out.println(nums.get(i));
        }*/

/*

        Map<Integer,Integer> numsMap = new HashMap<Integer,Integer>();
        for (int i=1;i<=37;i++){
            numsMap.put(i,i);

        }
        numsMap.remove(3);
        System.out.println(numsMap);
*/

    }
}