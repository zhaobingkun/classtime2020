package com.classtime.springcloud.entities;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class JdbcTemplate {
    private static JdbcTemplate jdbcTemplate = null;

    private JdbcTemplate(){ }

    public static JdbcTemplate getInstance() {
        if(jdbcTemplate == null)
            jdbcTemplate = new JdbcTemplate();
        return jdbcTemplate;
    }

    public boolean insert(String sql){
        boolean f = false;
        PreparedStatement prep = null;
        Connection conn = null;
        try {
            conn = DBPoolHelper.getConnection();
            prep = conn.prepareStatement(sql);
            f = prep.execute();

        } catch (SQLException e) {
            //logger.error("[JDBC Exception] --> "
              //      + "Can not insert, the exceprion message is:" + e.getMessage());
        } finally {
            try {
                if(null != prep)
                    prep.close();
            } catch (SQLException e) {
                //logger.error("[JDBC Exception] --> "
                  //      + "Failed to close connection, the exceprion message is:" + e.getMessage());
            }
        }

        return f;
    }
}
