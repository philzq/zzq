package zzq.simple.spring.mybatis.spring;

public class ApplicationContext extends Ioc {

    public <T> T getBean(Class<T> tClass){
        return (T)BEAN_MAP.get(tClass);
    }
}
