package zzq.simple.main;

import zzq.simple.main.entity.OrderLog;
import zzq.simple.main.mapper.OrderLogMapper;
import zzq.simple.mybatis.sqlSession.SqlSession;
import zzq.simple.mybatis.sqlSession.SqlSessionFactory;
import zzq.simple.mybatis.sqlSession.SqlSessionFactoryBuilder;

import java.util.ResourceBundle;

public class SimpleMybatis {

    public static void main(String[] args) {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("mybatis-config");
        //获取SqlSessionFactory实例
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(resourceBundle);
        //获取sqlSession实例
        SqlSession sqlSession = sqlSessionFactory.openSession();
        //获取代理Mapper对象
        OrderLogMapper orderLogMapper = sqlSession.getMapper(OrderLogMapper.class);
        //根据日期id获取指定的日志
        OrderLog orderLog = orderLogMapper.getOrderLogByLogID(2550L);
        System.out.println(orderLog);
    }

}
