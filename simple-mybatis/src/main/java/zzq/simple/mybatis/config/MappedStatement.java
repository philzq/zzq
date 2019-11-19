package zzq.simple.mybatis.config;

import lombok.Data;

@Data
public class MappedStatement {
    private String resource; //资源地址
	private String sqltype;  //sql的类型,计划在xml读取有四种情况
    private String funcName;  // 方法名
    private String sql;       //执行的sql语句
    private Object resultType;  // 返回类型
    private String parameterType; //参数类型
}
