package zzq.simple.spring.mybatis.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zzq.simple.spring.mybatis.annotation.Autowired;
import zzq.simple.spring.mybatis.annotation.MapperScan;
import zzq.simple.spring.mybatis.annotation.Service;
import zzq.simple.spring.mybatis.bind.MapperProxyFactory;

import java.io.File;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ApplicationContext {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationContext.class);

    public static final Map<Class<?>, Object> BEAN_MAP = new HashMap<Class<?>, Object>();

    static {
        logger.info("----------------初始化容器开始--------------");
        logger.info("根据启动类上的MapperScan注解的包路径值,加载该包下所有的Mapper接口到容器中");
        doScanMapper();
        logger.info("加载启动类当前包下所有包涵Service注解的类到容器中");
        doScanService();
        //处理依赖注入
        logger.info("处理依赖注入--动态为容器中对象，包涵Autowired注解的属性赋值");
        doDependencyInjection();
        logger.info("----------------初始化容器结束--------------");
    }

    /**
     * 处理依赖注入
     */
    private static void doDependencyInjection() {
        for (Map.Entry<Class<?>, Object> beanEntry : BEAN_MAP.entrySet()) {
            //bean的class类
            Class<?> beanClass = beanEntry.getKey();
            //bean的实例
            Object beanInstance = beanEntry.getValue();
            //暴力反射获取属性
            Field[] beanFields = beanClass.getDeclaredFields();
            //遍历bean的属性
            if (beanFields != null) {
                for (Field beanField : beanFields) {
                    //判断属性是否带Autowired注解
                    if (beanField.isAnnotationPresent(Autowired.class)) {
                        //属性类型
                        Class<?> beanFieldClass = beanField.getType();
                        //获取Class类对应的实例
                        Object beanFieldInstance = BEAN_MAP.get(beanFieldClass);
                        if (beanFieldInstance != null) {
                            setField(beanInstance, beanField, beanFieldInstance);
                        }
                    }
                }
            }
        }
    }

    public <T> T getBean(Class<T> tClass) {
        logger.info("从容器中获取" + tClass.getSimpleName() + "对象");
        return (T) BEAN_MAP.get(tClass);
    }

    /**
     * 扫描接口
     */
    private static void doScanMapper() {
        try {
            String mainClass = getMainClassName(Thread.currentThread());
            Class<?> clazz = Class.forName(mainClass);
            MapperScan mapperScan = clazz.getAnnotation(MapperScan.class);
            if (mapperScan != null) {
                String[] values = mapperScan.value();
                if (values != null) {
                    for (String string : values) {
                        doScanMapper(string);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("加载Mapper接口失败", e);
        }
    }

    /**
     * Description:  扫描指定包下的所有类
     *
     * @param packageName: 需要扫描的包名
     */
    private static void doScanMapper(String packageName) throws Exception {
        URL url = getClassLoader().getResource(packageName.replaceAll("\\.", "/"));
        if (url == null) {
            return;
        }
        File dir = new File(url.getFile());
        for (File file : dir.listFiles()) {
            if (file.isDirectory()) {
                //递归读取包
                doScanMapper(packageName + "." + file.getName());
            } else {
                String className = packageName + "." + file.getName().replace(".class", "");
                Class<?> clazz = Class.forName(className);
                BEAN_MAP.put(clazz, new MapperProxyFactory<>(clazz).newInstance());
            }
        }
    }

    /**
     * 加载包涵Service注解的类到容器中
     */
    private static void doScanService() {
        try {
            String mainClass = getMainClassName(Thread.currentThread());
            Class<?> clazz = Class.forName(mainClass);
            Package aPackage = clazz.getPackage();
            String packageName = aPackage.getName();
            doScanService(packageName);
        } catch (Exception e) {
            logger.error("加载包涵Service注解的类到容器中失败", e);
        }
    }

    /**
     * Description:  扫描指定包下的所有类
     *
     * @param packageName: 需要扫描的包名
     */
    private static void doScanService(String packageName) throws Exception {
        URL url = getClassLoader().getResource(packageName.replaceAll("\\.", "/"));
        if (url == null) {
            return;
        }
        File dir = new File(url.getFile());
        for (File file : dir.listFiles()) {
            if (file.isDirectory()) {
                //递归读取包
                doScanService(packageName + "." + file.getName());
            } else {
                String className = packageName + "." + file.getName().replace(".class", "");
                Class<?> clazz = Class.forName(className);
                Service service = clazz.getAnnotation(Service.class);
                if (service != null) {
                    BEAN_MAP.put(clazz, newInstance(clazz));
                }
            }
        }
    }

    /**
     * 获取启动类全限定名
     *
     * @param thread
     * @return
     */
    private static String getMainClassName(Thread thread) {
        for (StackTraceElement element : thread.getStackTrace()) {
            if ("main".equals(element.getMethodName())) {
                return element.getClassName();
            }
        }
        throw new IllegalStateException("Unable to find main method");
    }

    /**
     * 获取类加载器
     */
    public static ClassLoader getClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    /**
     * 创建实例
     */
    public static Object newInstance(Class<?> cls) {
        Object instance;
        try {
            instance = cls.getConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("new instance failure", e);
        }
        return instance;
    }

    /**
     * 设置成员变量的值
     */
    public static void setField(Object obj, Field field, Object value) {
        try {
            field.setAccessible(true); //去除私有权限
            field.set(obj, value);
        } catch (Exception e) {
            throw new RuntimeException("set field failure", e);
        }
    }
}
