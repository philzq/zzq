package zzq.simple.mybatis;

import org.dom4j.Document;
import org.dom4j.io.SAXReader;
import org.mariadb.jdbc.MariaDbDataSource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import zzq.simple.main.entity.OrderLog;
import zzq.simple.main.mapper.OrderLogMapper;
import zzq.simple.mybatis.annotation.MapperScan;
import zzq.simple.mybatis.binding.MapperRegistry;
import zzq.simple.mybatis.config.MapperBean;
import zzq.simple.mybatis.executor.Executor;
import zzq.simple.mybatis.mapper.ClassPathMapperScanner;
import zzq.simple.mybatis.sqlSession.Configuration;
import zzq.simple.mybatis.sqlSession.SqlSession;
import zzq.simple.mybatis.transaction.JdbcTransaction;
import zzq.simple.mybatis.xml.XMLMapperBuilder;

import javax.sql.DataSource;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.util.List;
import java.util.Properties;
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

        SqlSession sqlSession = new SqlSession(configuration,new Executor(jdbcTransaction));
        OrderLogMapper orderLogMapper = configuration.getMapperRegistry().getMapper(OrderLogMapper.class, sqlSession);
        List<OrderLog> orderLogs = orderLogMapper.getOrderLogs();
        System.out.println(orderLogs);
    }

}
