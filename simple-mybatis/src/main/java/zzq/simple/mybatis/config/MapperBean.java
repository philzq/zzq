package zzq.simple.mybatis.config;

import lombok.Data;

import java.util.List;

@Data
public class MapperBean {
	
	private String interfaceName; //接口名
	
    private List<Function> list; //接口下所有方法
}
