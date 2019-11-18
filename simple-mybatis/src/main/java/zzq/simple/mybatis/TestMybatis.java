package zzq.simple.mybatis;

import zzq.simple.mybatis.entity.OrderLog;
import zzq.simple.mybatis.mapper.OrderLogMapper;
import zzq.simple.mybatis.sqlSession.MySqlsession;

import java.util.List;

public class TestMybatis {
	
    public static void main(String[] args) {  
        MySqlsession sqlsession=new MySqlsession();
        OrderLogMapper mapper = sqlsession.getMapper(OrderLogMapper.class);
        List<OrderLog> orderLogs = mapper.getOrderLogs();
        System.out.println(orderLogs);
    } 

}
