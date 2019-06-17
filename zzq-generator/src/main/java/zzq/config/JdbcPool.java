package zzq.config;

import cn.hutool.json.JSONUtil;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据库操作封装类
 * @author Dawn
 *
 */
public class JdbcPool {

    static Logger logger = LogManager.getLogger();
    Connection connection = null;

    //-- Hikari Datasource -->
    //driverClassName无需指定，除非系统无法自动识别
    private static String driverClassName="com.mysql.cj.jdbc.Driver";
    //database address
    private static String  jdbcUrl="***";
    //useName 用户名
    private static String username="***";
    //password
    private static String  password="***";
    //连接只读数据库时配置为true， 保证安全 -->
    private static boolean readOnly=false;
    //等待连接池分配连接的最大时长（毫秒），超过这个时长还没可用的连接则发生SQLException， 缺省:30秒 -->
    private static int connectionTimeout=30000;
    // 一个连接idle状态的最大时长（毫秒），超时则被释放（retired），缺省:10分钟 -->
    private static int idleTimeout=600000;
    //一个连接的生命时长（毫秒），超时而且没被使用则被释放（retired），缺省:30分钟，建议设置比数据库超时时长少30秒，参考MySQL wait_timeout参数（show variables like '%timeout%';） -->
    private static int maxLifetime=1800000;
    // 连接池中允许的最大连接数。缺省值：10；推荐的公式：((core_count * 2) + effective_spindle_count) -->
    private static int maximumPoolSize=10;
    static  HikariDataSource hikariDataSource = new HikariDataSource();

    static {
        //driverClassName无需指定，除非系统无法自动识别
        hikariDataSource.setDriverClassName(driverClassName);
        //database address
        hikariDataSource.setJdbcUrl(jdbcUrl);
        //useName 用户名
        hikariDataSource.setUsername(username);
        //password
        hikariDataSource.setPassword(password);
        //连接只读数据库时配置为true， 保证安全 -->
        hikariDataSource.setReadOnly(readOnly);
        //等待连接池分配连接的最大时长（毫秒），超过这个时长还没可用的连接则发生SQLException， 缺省:30秒 -->
        hikariDataSource.setConnectionTimeout(connectionTimeout);
        // 一个连接idle状态的最大时长（毫秒），超时则被释放（retired），缺省:10分钟 -->
        hikariDataSource.setIdleTimeout(idleTimeout);
        //一个连接的生命时长（毫秒），超时而且没被使用则被释放（retired），缺省:30分钟，建议设置比数据库超时时长少30秒，参考MySQL wait_timeout参数（show variables like '%timeout%';） -->
        hikariDataSource.setMaximumPoolSize(maxLifetime);
        // 连接池中允许的最大连接数。缺省值：10；推荐的公式：((core_count * 2) + effective_spindle_count) -->
        hikariDataSource.setMaximumPoolSize(maximumPoolSize);
    }

    /**
     * 取得数据库连接
     * @return
     * @throws Exception
     */
    public  void getConnection(){
        try {
            connection = hikariDataSource.getConnection();
        } catch (Exception e) {
            logger.error("取得数据库连接时发生异常!"+ e);
        }
    }

    /**
     * 释放数据库连接
     */
    public  void freeConnection(){
        if (connection != null){
            try {
                //onnection的实现类（代理类）自己会调用hikariPool.evictConnection(cn) ,将此连接回收到pool中
                connection.close();
            }catch(Exception e){
                logger.error("释放数据库连接时发生异常!"+ e.getMessage());
            }
        }
    }

    /**
     * 查询没有任何条件
     * @param sql
     * @return
     */
    public ResultSet excuteQuery(String sql) {
        return excuteQuery(sql, null);
    }

    /**
     * 查询时传一个数据参数
     * @param sql
     * @param obj
     * @return
     */
    public ResultSet excuteQuery(String sql,Object obj) {
        Object[] objs=new Object[1];
        objs[0]=obj;
        return excuteQuery(sql, objs);
    }

    /**
     * 查询时传递多个参数
     * @param sql
     * @param objs
     * @return
     */
    public ResultSet excuteQuery(String sql,Object[] objs) {
        PreparedStatement ps = null;
        ResultSet rs=null;
        try {
            getConnection();
            ps=connection.prepareStatement(sql);
            if(objs!=null) {
                for(int i=0;i<objs.length;i++) {
                    ps.setObject(i+1, objs[i]);
                }
            }
            rs=ps.executeQuery();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.out.println("sql查询出现错误...");
        }
        return rs;
    }

    /**
     * DML操作，增删改操作
     * @param sql
     * @return
     */
    public int excuteUpdate(String sql) {
        return excuteUpdate(sql, null);
    }

    /**
     * DML操作，增删改操作,传递一个参数
     * @param sql
     * @return
     */
    public int excuteUpdate(String sql,Object obj) {
        Object[] objs=new Object[1];
        objs[0]=obj;
        return excuteUpdate(sql, objs);
    }

    /**
     * DML操作，增删改操作,传递多个参数
     * @param sql
     * @return
     */
    public int excuteUpdate(String sql,Object[] params) {
        int rtn = 0;
        PreparedStatement ps = null;

        try {
            getConnection();
            ps=connection.prepareStatement(sql);
            connection.setAutoCommit(false);
            if(params != null && params.length > 0) {
                for(int i = 0; i < params.length; i++) {
                    ps.setObject(i + 1, params[i]);
                }
            }

            rtn = ps.executeUpdate();

            connection.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return rtn;
    }
    public List<Map<String, Object>> getResult(ResultSet rs){
        List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
        try {
            ResultSetMetaData md = rs.getMetaData();
            int columnCount = md.getColumnCount();
            while (rs.next()) {
                Map<String,Object> rowData = new HashMap<String,Object>();
                for (int i = 1; i <= columnCount; i++) {
                    rowData.put(md.getColumnLabel(i), rs.getObject(i));
                }
                list.add(rowData);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static void main(String[] args){
        JdbcPool jdbcPool=new JdbcPool();
        ResultSet rs=jdbcPool.excuteQuery("");
        List result=jdbcPool.getResult(rs);
        System.out.println(result.toString());
        jdbcPool.freeConnection();
    }

}

