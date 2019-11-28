package zzq.simple.spring.mybatis.mapper;

import zzq.simple.spring.mybatis.annotation.Select;
import zzq.simple.spring.mybatis.entity.OrderLog;

import java.util.List;

public interface OrderLogMapper {

    @Select("select * from OrderLog where logID = #{logID} ")
    OrderLog getOrderLogByLogID(Long logID);
}
