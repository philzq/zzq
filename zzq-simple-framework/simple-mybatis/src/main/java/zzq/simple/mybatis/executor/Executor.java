package zzq.simple.mybatis.executor;

import zzq.simple.main.entity.OrderLog;
import zzq.simple.mybatis.xml.MappedStatement;
import zzq.simple.mybatis.transaction.JdbcTransaction;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class Executor{

    private JdbcTransaction jdbcTransaction;

    public Executor(JdbcTransaction jdbcTransaction) {
        this.jdbcTransaction = jdbcTransaction;
    }

    public <T> T selectOne(MappedStatement mappedStatement, Object parameter) throws Exception{
        // Popular vote was to return null on 0 results and throw exception on too many.
        List<T> list = this.selectList(mappedStatement, parameter);
        if (list.size() == 1) {
            return list.get(0);
        } else if (list.size() > 1) {
            throw new RuntimeException("Expected one result (or null) to be returned by selectOne(), but found: " + list.size());
        } else {
            return null;
        }
    }

    public <T> List<T> selectList(MappedStatement mappedStatement, Object parameter) throws SQLException{
        Connection connection = jdbcTransaction.getConnection();
        ResultSet set = null;
        PreparedStatement pre = null;
        try {
            pre = connection.prepareStatement(mappedStatement.getSql());
            if(parameter!=null){
                //设置参数
                pre.setString(1, parameter.toString());
            }
            set = pre.executeQuery();
            List<Object> list = new ArrayList<>();
            //遍历结果集
            while (set.next()) {
                String resultType = mappedStatement.getResultType();
                Object object = null;
                try {
                    object = Class.forName(resultType).getConstructor().newInstance();
                    Field[] fields = object.getClass().getDeclaredFields();
                    if(fields!=null){
                        for(Field field : fields){
                            field.setAccessible(true);
                            String fieldValue = null;
                            try {
                                fieldValue = set.getString(field.getName());
                            }catch (Exception e){
                                e.getStackTrace();
                            }
                            if(fieldValue!=null){
                                field.set(object,Long.valueOf(fieldValue));
                            }
                        }
                    }
                    list.add(object);
                }catch (Exception e){
                    e.getStackTrace();
                }
            }
            return (List<T>) list;
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

    public <T> T mockSelectOne(MappedStatement mappedStatement, Object parameter){
        OrderLog orderLog = new OrderLog();
        orderLog.setLogID((Long) parameter);
        return (T)orderLog;
    }

    public <T> List<T> mockSelectList(MappedStatement mappedStatement, Object parameter){
        List<Object> list = new ArrayList<>();
        for(long i=0;i<10;i++){
            OrderLog orderLog = new OrderLog();
            orderLog.setLogID(i);
            list.add(orderLog);
        }
        return (List<T>)list;
    }

}
