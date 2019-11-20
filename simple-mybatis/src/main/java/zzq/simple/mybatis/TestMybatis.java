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
        /****************************初始化过程**********************************/
        Configuration configuration = new Configuration();
        ResourceBundle resourceBundle = ResourceBundle.getBundle("mybatis-config");
        //加载数据源
        MariaDbDataSource mariaDbDataSource = new MariaDbDataSource();
        mariaDbDataSource.setUrl(resourceBundle.getString("jdbc.url"));
        mariaDbDataSource.setUserName(resourceBundle.getString("jdbc.userName"));
        mariaDbDataSource.setPassword(resourceBundle.getString("jdbc.passWord"));
        //加载接口
        ClassPathMapperScanner classPathMapperScanner = new ClassPathMapperScanner(configuration);
        classPathMapperScanner.doScanMapper();
        //加载mapper.xml
        XMLMapperBuilder xmlMapperBuilder = new XMLMapperBuilder(configuration);
        Resource[] mapperLocations = new PathMatchingResourcePatternResolver()
                .getResources(resourceBundle.getString("mapperLocation"));
        xmlMapperBuilder.readMapper(mapperLocations);

        /*****************************执行过程**********************************/
        //创建JdbcTransaction事物实例
        JdbcTransaction jdbcTransaction = new JdbcTransaction(mariaDbDataSource);
        //创建执行器实例
        Executor executor = new Executor(jdbcTransaction);
        //创建SqlSession实例
        SqlSession sqlSession = new SqlSession(configuration, executor);
        //获取代理Mapper对象
        OrderLogMapper orderLogMapper = sqlSession.getMapper(OrderLogMapper.class);
        //根据日期id获取指定的日志
        OrderLog orderLog = orderLogMapper.getOrderLogByLogID(9000L);
        System.out.println(orderLog);
    }

}
