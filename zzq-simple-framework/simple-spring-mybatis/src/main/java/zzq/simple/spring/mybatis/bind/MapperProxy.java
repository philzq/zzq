package zzq.simple.spring.mybatis.bind;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zzq.simple.spring.mybatis.annotation.Select;
import zzq.simple.spring.mybatis.session.SqlSession;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class MapperProxy<T> implements InvocationHandler{

    private static final Logger logger = LoggerFactory.getLogger(MapperProxy.class);

    @Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Exception{
        logger.info("---------------------执行DB操作开始------------------------------");
        // hashCode()、toString()、equals()等方法，调用原生方法
        if (Object.class.equals(method.getDeclaringClass())) {
            return method.invoke(this, args);
        }

        // 其他方法拦截下来，由“代理”决定怎么响应。是查数据库还是缓存 “代理说了算”。

        // ============== Step 1: 从“注解”或“xml”或“解析domain”拿到对应的SQL ==============
        logger.info("方法的签名:" + method.toString()); // 从xml拿: 可以根据签名去匹配
        var sql = method.getDeclaredAnnotation(Select.class).value(); // 从注解拿: 反射就行了
        logger.info("注解上写的Sql:" + sql);


        // ============== Step 2: 从Method上拿到参数的注解、参数名、参数值，进行替换，生成可执行的SQL ==============
        var params = method.getParameters();
        for (int i = 0; i < params.length; i++) {
            var param = method.getParameters()[i];
            String paramName = param.getName();
            logger.info("参数类型:{} 参数名称:{}  参数值:{}", param.getType(), paramName, args[i]);
            sql = sql.replace("#{" + paramName + "}", args[i].toString());
        }

        // ============== Step 3: 查数据库 ==============
        logger.info("调用JDBC执行最终的Sql:" + sql);
        Object result = new SqlSession().mockSelectOne(args[0]);

        // ============== Step 4: 将DB结果转为实体 ==============
        //注:此处mock的数据，不用处理结果集
        logger.info("---------------------执行DB操作结束------------------------------");
        return result;
	}
	

}
