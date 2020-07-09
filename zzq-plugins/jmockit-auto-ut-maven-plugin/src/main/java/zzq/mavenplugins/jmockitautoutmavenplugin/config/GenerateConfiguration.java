package zzq.mavenplugins.jmockitautoutmavenplugin.config;

import javassist.CtClass;
import javassist.CtMethod;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 该类会存储生成测试用例的所有数据
 */
public class GenerateConfiguration {

    private volatile static GenerateConfiguration generateConfiguration;

    /**
     * 存储所有扫描类的方法依赖
     */
    private Map<CtClass, Map<CtMethod, Set<CtMethod>>> methodDependency = new HashMap<>();

    private GenerateConfiguration(){};

    /**
     * 获取实例
     * @return
     */
    public static GenerateConfiguration getInstance(){
        if(generateConfiguration == null){
            synchronized (GenerateConfiguration.class){
                if(generateConfiguration == null){
                    generateConfiguration = new GenerateConfiguration();
                }
            }
        }
        return generateConfiguration;
    }

    /**
     * 添加类方法依赖
     * @param ctClass
     * @param methodSetMap
     */
    public void addClassMethodDependency(CtClass ctClass,Map<CtMethod, Set<CtMethod>> methodSetMap){
        methodDependency.put(ctClass,methodSetMap);
    }

    /**
     * 获取方法依赖
     * @return
     */
    public Map<CtClass, Map<CtMethod, Set<CtMethod>>> getMethodDependency() {
        return methodDependency;
    }
}
