package zzq.simple.mybatis;

import org.mariadb.jdbc.MariaDbDataSource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import zzq.simple.main.entity.OrderLog;
import zzq.simple.main.mapper.OrderLogMapper;
import zzq.simple.mybatis.annotation.MapperScan;
import zzq.simple.mybatis.executor.Executor;
import zzq.simple.mybatis.mapper.ClassPathMapperScanner;
import zzq.simple.mybatis.sqlSession.Configuration;
import zzq.simple.mybatis.sqlSession.SqlSession;
import zzq.simple.mybatis.transaction.JdbcTransaction;
import zzq.simple.mybatis.xml.XMLMapperBuilder;

import java.util.ResourceBundle;

@MapperScan("zzq.simple.main.mapper")
public class TestMybatis {

    public static void main(String[] args) throws Exception {
        Configuration configuration = new Configuration();

        ResourceBundle resourceBundle = ResourceBundle.getBundle("mybatis-config");

        //加载数据源
        MariaDbDataSource mariaDbDataSource = new MariaDbDataSource();
        mariaDbDataSource.setUrl(resourceBundle.getString("jdbc.url"));
        mariaDbDataSource.setUserName(resourceBundle.getString("jdbc.userName"));
        mariaDbDataSource.setPassword(resourceBundle.getString("jdbc.passWord"));
        JdbcTransaction jdbcTransaction = new JdbcTransaction(mariaDbDataSource);
        //加载接口
        ClassPathMapperScanner classPathMapperScanner = new ClassPathMapperScanner(configuration);
        classPathMapperScanner.doScanMapper();
        //加载mapper.xml
        XMLMapperBuilder xmlMapperBuilder = new XMLMapperBuilder(configuration);
        Resource[] mapperLocations = new PathMatchingResourcePatternResolver()
                .getResources(resourceBundle.getString("mapperLocation"));
        xmlMapperBuilder.readMapper(mapperLocations);

        //创建sql会话
        SqlSession sqlSession = new SqlSession(configuration,new Executor(jdbcTransaction));

        //获取代理对象
        OrderLogMapper orderLogMapper = configuration.getMapperRegistry().getMapper(OrderLogMapper.class, sqlSession);
        //获取所有日志
//        List<OrderLog> orderLogs = orderLogMapper.getOrderLogs();
//        System.out.println(orderLogs);
        //根据日期id获取指定的日志
        OrderLog orderLog = orderLogMapper.getOrderLogByLogID(40L);
        System.out.println(orderLog);
    }

}
