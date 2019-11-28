package zzq.simple.spring.mybatis;

import zzq.simple.spring.mybatis.entity.OrderLog;
import zzq.simple.spring.mybatis.service.OrderLogService;
import zzq.simple.spring.mybatis.spring.ApplicationContext;

public class SimpleSpringMybatisApplication {
    
    public static void main(String[] args){
        //初始化容器
        ApplicationContext applicationContext = new ApplicationContext();
        //从容器中获取OrderLogService对象
        OrderLogService orderLogService = applicationContext.getBean(OrderLogService.class);
        //执行通过日志id获取日志的方法
        OrderLog orderLog = orderLogService.getOrderLogByLogID(456L);
        //打印结果
        System.out.println(orderLog);
    }
}
