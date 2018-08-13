package net.beeapm.demo.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

/**
 * Created by yuan on 2018/8/2.
 */
public class DbServiceImpl{
    public Connection getConnction(){
        String driver = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/tes2222";
        String username = "root";
        String password = "root123";
        Connection conn = null;
        try {
            Class.forName(driver); //classLoader,加载对应驱动
            conn = DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    public void queryAll() {
        Connection conn = getConnction();
        String sql = "select * from people t where t.id in (?,?)";
        PreparedStatement pstmt;
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1,2L);
            pstmt.setLong(2,3L);
            ResultSet rs = pstmt.executeQuery();
            int col = rs.getMetaData().getColumnCount();
            while (rs.next()) {
                StringBuilder sb = new StringBuilder();
                for (int i = 1; i <= col; i++) {
                    sb.append(rs.getObject(i).toString()).append("  ");
                }
                System.out.println("=================>"+sb.toString());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
