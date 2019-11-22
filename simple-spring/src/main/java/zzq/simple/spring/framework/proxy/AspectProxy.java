package zzq.simple.spring.framework.proxy;

import java.lang.reflect.Method;

/**
 * 切面代理 抽象类
 */
public abstract class AspectProxy implements Proxy {

    @Override
    public final Object doProxy(ProxyChain proxyChain) throws Throwable {
        Object result = null;

        Class<?> cls = proxyChain.getTargetClass();
        Method method = proxyChain.getTargetMethod();
        Object[] params = proxyChain.getMethodParams();

        begin();
        try {
            if (intercept(method, params)) {
                before(method, params);
                result = proxyChain.doProxyChain();
                after(method, params);
            } else {
                result = proxyChain.doProxyChain();
            }
        } catch (Exception e) {
            error(method, params, e);
            throw e;
        } finally {
            end();
        }

        return result;
    }

    /**
     * 开始增强
     */
    public void begin() {
    }

    /**
     * 切入点判断
     */
    public boolean intercept(Method method, Object[] params) throws Throwable {
        return true;
    }

    /**
     * 前置增强
     */
    public void before(Method method, Object[] params) throws Throwable {
    }

    /**
     * 后置增强
     */
    public void after(Method method, Object[] params) throws Throwable {
    }

    /**
     * 异常增强
     */
    public void error(Method method, Object[] params, Throwable e) {
    }

    /**
     * 最终增强
     */
    public void end() {
    }
}