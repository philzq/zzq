package zzq.simple.mybatis.binding;

import zzq.simple.mybatis.executor.parameter.ParamNameResolver;
import zzq.simple.mybatis.sqlSession.Configuration;
import zzq.simple.mybatis.sqlSession.SqlSession;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class MapperProxy<T> implements InvocationHandler{

	private final SqlSession sqlsession;

    private final Class<T> mapperInterface;

    private final Configuration configuration;

    public MapperProxy(SqlSession sqlsession, Class<T> mapperInterface, Configuration configuration) {
        this.sqlsession = sqlsession;
        this.mapperInterface = mapperInterface;
        this.configuration = configuration;
    }

    @Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String statement = method.getDeclaringClass().getName() + "." + method.getName();
        ParamNameResolver paramNameResolver = new ParamNameResolver(configuration, method);
        Object params = paramNameResolver.getNamedParams(args);
        return this.sqlsession.selectOne(statement,params);
	}
	

}
