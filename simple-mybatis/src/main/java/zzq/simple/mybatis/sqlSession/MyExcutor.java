package zzq.simple.mybatis.sqlSession;

import zzq.simple.mybatis.entity.OrderLog;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class MyExcutor implements Excutor {

    private MyConfiguration xmlConfiguration = new MyConfiguration();

    @Override
    public <T> T query(String sql, Object parameter) {
        Connection connection = getConnection();
        ResultSet set = null;
        PreparedStatement pre = null;
        try {
            pre = connection.prepareStatement(sql);
            if(parameter!=null){
                //设置参数
                pre.setString(1, parameter.toString());
            }
            set = pre.executeQuery();
            List<OrderLog> orderLogs = new ArrayList<>();
            //遍历结果集
            while (set.next()) {
                OrderLog orderLog = OrderLog.builder()
                        .logID(set.getLong(1))
                        .build();
                orderLogs.add(orderLog);
            }
            return (T) orderLogs;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (set != null) {
                    set.close();
                }
                if (pre != null) {
                    pre.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return null;
    }

    private Connection getConnection() {
        try {
            Connection connection = xmlConfiguration.build("config.xml");
            return connection;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
