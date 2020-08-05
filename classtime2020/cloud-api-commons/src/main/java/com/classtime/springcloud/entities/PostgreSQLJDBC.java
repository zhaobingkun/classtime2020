package com.classtime.springcloud.entities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class PostgreSQLJDBC {
    public static void main(String args[]) {

        Connection conn = null;
        Statement stmt = null;
        try {
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection("jdbc:postgresql://172.17.2.22:5432/gpdw", "gpadmin", "gpadmin");
            conn.setAutoCommit(false); // 把自动提交
            System.out.println("Opened database successfully");
/*
            stmt = c.createStatement();
            String sql = "CREATE TABLE STUDENTS " +
                    "(ID TEXT PRIMARY KEY     NOT NULL ," +
                    " NAME            TEXT    NOT NULL, " +
                    " SEX             TEXT    NOT NULL, " +
                    " AGE             TEXT    NOT NULL)";
            stmt.executeUpdate(sql);
            System.out.println("Table created successfully");*/

            //stmt.close();
            //conn.commit();
            conn.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }
}
