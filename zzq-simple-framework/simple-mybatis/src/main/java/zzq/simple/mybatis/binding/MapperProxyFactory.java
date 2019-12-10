package zzq.simple.mybatis.binding;

import zzq.simple.mybatis.sqlSession.Configuration;
import zzq.simple.mybatis.sqlSession.SqlSession;

import java.lang.reflect.Proxy;

public class MapperProxyFactory<T> {

    private final Class<T> mapperInterface;

    private final Configuration configuration;

    public MapperProxyFactory(Class<T> mapperInterface, Configuration configuration) {
        this.mapperInterface = mapperInterface;
        this.configuration = configuration;
    }

    public Class<T> getMapperInterface() {
        return mapperInterface;
    }


    @SuppressWarnings("unchecked")
    protected T newInstance(MapperProxy<T> mapperProxy) {
        return (T) Proxy.newProxyInstance(mapperInterface.getClassLoader(), new Class[] { mapperInterface }, mapperProxy);
    }

    public T newInstance(SqlSession sqlSession) {
        final MapperProxy<T> mapperProxy = new MapperProxy<T>(sqlSession, mapperInterface,configuration);
        return newInstance(mapperProxy);
    }
}
