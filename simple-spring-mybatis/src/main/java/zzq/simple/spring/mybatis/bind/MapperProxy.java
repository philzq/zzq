package zzq.simple.spring.mybatis.bind;

import zzq.simple.spring.mybatis.session.SqlSession;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class MapperProxy<T> implements InvocationHandler{

    @Override
	public Object invoke(Object proxy, Method method, Object[] args){
        return new SqlSession().mockSelectOne(args[0]);
	}
	

}
