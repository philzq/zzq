package zzq.config;

import com.zaxxer.hikari.HikariDataSource;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
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

    private static Logger logger = LogManager.getLogger();
    private Connection connection = null;

    private static Configuration config = getJdbcConfig();

    private static  HikariDataSource hikariDataSource = new HikariDataSource();

    static {
        //driverClassName无需指定，除非系统无法自动识别
        hikariDataSource.setDriverClassName(config.getString("driverClassName"));
        //database address
        hikariDataSource.setJdbcUrl(config.getString("jdbcUrl"));
        //useName 用户名
        hikariDataSource.setUsername(config.getString("username"));
        //password
        hikariDataSource.setPassword(config.getString("password"));
        //连接只读数据库时配置为true， 保证安全 -->
        hikariDataSource.setReadOnly(config.getBoolean("readOnly"));
        //等待连接池分配连接的最大时长（毫秒），超过这个时长还没可用的连接则发生SQLException， 缺省:30秒 -->
        hikariDataSource.setConnectionTimeout(config.getLong("connectionTimeout"));
        // 一个连接idle状态的最大时长（毫秒），超时则被释放（retired），缺省:10分钟 -->
        hikariDataSource.setIdleTimeout(config.getLong("idleTimeout"));
        //一个连接的生命时长（毫秒），超时而且没被使用则被释放（retired），缺省:30分钟，建议设置比数据库超时时长少30秒，参考MySQL wait_timeout参数（show variables like '%timeout%';） -->
        hikariDataSource.setMaximumPoolSize(config.getInt("maxLifetime"));
        // 连接池中允许的最大连接数。缺省值：10；推荐的公式：((core_count * 2) + effective_spindle_count) -->
        hikariDataSource.setMaximumPoolSize(config.getInt("maximumPoolSize"));
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

    /**
     * 获取配置信息
     */
    public static Configuration getJdbcConfig(){
        try {
            return new PropertiesConfiguration("jdbc.properties");
        } catch (ConfigurationException e) {
            throw new RuntimeException("获取配置文件失败");
        }
    }
}

