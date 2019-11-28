package zzq.simple.spring.mybatis.mapper;

import zzq.simple.spring.mybatis.entity.OrderLog;

import java.util.List;

public interface OrderLogMapper {

    OrderLog getOrderLogByLogID(Long logID);
}
