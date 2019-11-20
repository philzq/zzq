package zzq.simple.mybatis.mapper;

import zzq.simple.mybatis.SimpleMybatis;
import zzq.simple.mybatis.annotation.MapperScan;
import zzq.simple.mybatis.common.Restarter;
import zzq.simple.mybatis.sqlSession.Configuration;

import java.io.File;
import java.net.URL;

/**
 * 加载mapper接口
 */
public class ClassPathMapperScanner {

    private final Configuration configuration;

    public ClassPathMapperScanner(Configuration configuration) {
        this.configuration = configuration;
    }

    /**
     * 扫描接口
     * @throws Exception
     */
    public void doScanMapper() throws Exception {
        String mainClass = Restarter.getMainClassName(Thread.currentThread());
        Class<?> clazz = Class.forName(mainClass);
        MapperScan mapperScan = clazz.getAnnotation(MapperScan.class);
        if (mapperScan != null) {
            String[] values = mapperScan.value();
            if (values != null) {
                for (String string : values) {
                    doScanner(string);
                }
            }
        }
    }

    /**
     * Description:  扫描指定包下的所有类
     *
     * @param packageName: 需要扫描的包名
     */
    private void doScanner(String packageName) throws Exception{
        URL url = SimpleMybatis.class.getClassLoader().getResource(packageName.replaceAll("\\.", "/"));
        if(url==null){
            return;
        }
        File dir = new File(url.getFile());
        for (File file : dir.listFiles()) {
            if (file.isDirectory()) {
                //递归读取包
                doScanner(packageName + "." + file.getName());
            } else {
                String className = packageName + "." + file.getName().replace(".class", "");
                configuration.addMapper(Class.forName(className));
            }
        }
    }
}
