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
import zzq.simple.mybatis.sqlSession.SqlSessionFactory;
import zzq.simple.mybatis.sqlSession.SqlSessionFactoryBuilder;
import zzq.simple.mybatis.transaction.JdbcTransaction;
import zzq.simple.mybatis.xml.XMLConfigBuilder;

import java.util.ResourceBundle;

@MapperScan("zzq.simple.main.mapper")
public class TestMybatis {

    public static void main(String[] args) throws Exception {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("mybatis-config");
        //获取SqlSessionFactory实例
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(resourceBundle);
        //获取sqlSession实例
        SqlSession sqlSession = sqlSessionFactory.openSession();
        //获取代理Mapper对象
        OrderLogMapper orderLogMapper = sqlSession.getMapper(OrderLogMapper.class);
        //根据日期id获取指定的日志
        OrderLog orderLog = orderLogMapper.getOrderLogByLogID(9000L);
        System.out.println(orderLog);
    }

}
