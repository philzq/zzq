package zzq.simple.spring.mybatis.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zzq.simple.spring.mybatis.annotation.Autowired;
import zzq.simple.spring.mybatis.annotation.Service;
import zzq.simple.spring.mybatis.entity.OrderLog;
import zzq.simple.spring.mybatis.mapper.OrderLogMapper;

@Service
public class OrderLogService {

    private static final Logger logger = LoggerFactory.getLogger(OrderLogService.class);

    @Autowired
    private OrderLogMapper orderLogMapper;

    public OrderLog getOrderLogByLogID(Long logID){
        OrderLog orderLog = orderLogMapper.getOrderLogByLogID(logID);
        logger.info("通过日志id:"+logID+",获取日志:"+orderLog);
        return orderLog;
    }
}
