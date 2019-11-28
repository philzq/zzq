package zzq.simple.spring.mybatis.service;

import zzq.simple.spring.mybatis.annotation.Autowired;
import zzq.simple.spring.mybatis.entity.OrderLog;
import zzq.simple.spring.mybatis.mapper.OrderLogMapper;

public class OrderLogService {

    @Autowired
    private OrderLogMapper orderLogMapper;

    public OrderLog getOrderLogByLogID(Long logID){
        OrderLog orderLogByLogID = orderLogMapper.getOrderLogByLogID(logID);
        return orderLogByLogID;
    }
}
