package com.classtime.springcloud.entities;


import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;

import javax.activation.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class DBPoolHelper {
    /**
     * 默认配置文件名
     */
    public static String confile = "druid.properties";
    /**
     * 配置文件
     */
    public static Properties p = null;
    /**
     * 唯一dateSource，保证全局只有一个数据库连接池
     */
    public static DruidDataSource dataSource = null;

    private static ThreadLocal<Connection> connectionThreadLocal = new ThreadLocal<Connection>();

    static {
        p = new Properties();
        InputStream inputStream = null;
        try {
            // java应用 读取配置文件
            inputStream = DBPoolHelper.class.getClassLoader().getResourceAsStream(confile);
            p.load(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                // ignore
            }
        } // end finally

        try {
            //通过工厂类获取DataSource对象
            dataSource = (DruidDataSource) DruidDataSourceFactory.createDataSource(p);
            // dataSource = (DruidDataSource)DruidDataSourceFactory.createDataSource(properties); //DruidDataSrouce工厂模式
        } catch (Exception e) {
            //logger.error("获取连接异常 ", e);
        }

    } // end static


    /**
     * 获取连接
     * @return
     */
    public static Connection getConnection() throws SQLException {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            throw new SQLException("获取连接时异常", e);
        }

    }


    /**
     * 关闭连接
     * @param con
     */
    public static void close(Connection con) throws SQLException {
        try {
            if (con != null) {
                con.close();
            }
        } catch (SQLException e) {
            throw new SQLException("关闭连接时异常", e);
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                throw new SQLException("关闭连接时异常", e);
            }
        }
    }

    /**
     *
     */
    public static void startTransaction() {
        Connection conn = connectionThreadLocal.get();
        try {
            if (conn == null) {
                conn = getConnection();
                connectionThreadLocal.set(conn);
            }
            conn.setAutoCommit(false);
        } catch (Exception e) {
            //logger.error("[JDBC Exception] --> "
             //       + "Failed to start the transaction, the exceprion message is:" + e.getMessage());
        }
    }

    /**
     *
     */
    public static void commit() {
        try {
            Connection conn = connectionThreadLocal.get();
            if (null != conn) {
                conn.commit();
            }
        } catch (Exception e) {
            //logger.error("[JDBC Exception] --> "
            //        + "Failed to commit the transaction, the exceprion message is:" + e.getMessage());
        }
    }

    /**
     *
     */
    public static void rollback() {
        try {
            Connection conn = connectionThreadLocal.get();
            if (conn != null) {
                conn.rollback();
                connectionThreadLocal.remove();
            }
        } catch (Exception e) {
            //logger.error("[JDBC Exception] --> "
            //        + "Failed to rollback the transaction, the exceprion message is:" + e.getMessage());
        }
    }


    // end method
}
