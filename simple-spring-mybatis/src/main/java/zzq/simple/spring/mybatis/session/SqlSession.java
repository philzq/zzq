package zzq.simple.spring.mybatis.session;


import zzq.simple.spring.mybatis.entity.OrderLog;

/**
 * The default implementation for {@link SqlSession}.
 * Note that this class is not Thread-Safe.
 *
 * @author Clinton Begin
 */
public class SqlSession {
    public <T> T mockSelectOne(Object parameter){
        OrderLog orderLog = new OrderLog();
        orderLog.setLogID((Long) parameter);
        return (T)orderLog;
    }
}
