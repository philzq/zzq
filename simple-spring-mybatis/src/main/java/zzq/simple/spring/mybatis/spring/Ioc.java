package zzq.simple.spring.mybatis.spring;

import zzq.simple.spring.mybatis.annotation.Autowired;
import zzq.simple.spring.mybatis.bind.MapperProxyFactory;
import zzq.simple.spring.mybatis.mapper.OrderLogMapper;
import zzq.simple.spring.mybatis.service.OrderLogService;
import zzq.simple.spring.mybatis.util.ReflectionUtil;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * 依赖注入助手类
 */
public class Ioc {

    public static final Map<Class<?>, Object> BEAN_MAP = new HashMap<Class<?>, Object>();

    static {
        //想容器中注入两个对象
        BEAN_MAP.put(OrderLogMapper.class,new MapperProxyFactory<OrderLogMapper>(OrderLogMapper.class).newInstance());
        BEAN_MAP.put(OrderLogService.class, ReflectionUtil.newInstance(OrderLogService.class));

        for (Map.Entry<Class<?>, Object> beanEntry : BEAN_MAP.entrySet()) {
            //bean的class类
            Class<?> beanClass = beanEntry.getKey();
            //bean的实例
            Object beanInstance = beanEntry.getValue();
            //暴力反射获取属性
            Field[] beanFields = beanClass.getDeclaredFields();
            //遍历bean的属性
            if (beanFields!=null) {
                for (Field beanField : beanFields) {
                    //判断属性是否带Autowired注解
                    if (beanField.isAnnotationPresent(Autowired.class)) {
                        //属性类型
                        Class<?> beanFieldClass = beanField.getType();
                        //获取Class类对应的实例
                        Object beanFieldInstance = BEAN_MAP.get(beanFieldClass);
                        if (beanFieldInstance != null) {
                            ReflectionUtil.setField(beanInstance, beanField, beanFieldInstance);
                        }
                    }
                }
            }
        }
    }

}
