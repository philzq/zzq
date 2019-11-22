package zzq.simple.main.aspect;

import zzq.simple.spring.framework.annotation.Aspect;
import zzq.simple.spring.framework.proxy.AspectProxy;

import java.lang.reflect.Method;

/**
 * 性能切面, 获取接口执行时间
 */
@Aspect(pkg = "zzq.simple.main.controller", cls = "DataController")
public class DataAspect extends AspectProxy {
    private long begin;

    /**
     * 切入点判断
     */
    @Override
    public boolean intercept(Method method, Object[] params) throws Throwable {
        return method.getName().equals("getData");
    }

    @Override
    public void before(Method method, Object[] params) throws Throwable {
        System.out.println("---------- begin ----------");
        begin = System.currentTimeMillis();
    }

    @Override
    public void after(Method method, Object[] params) throws Throwable {
        System.out.println(String.format("time: %dms", System.currentTimeMillis() - begin));
        System.out.println("----------- end -----------");
    }
}
