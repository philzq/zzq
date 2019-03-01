package zzq.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import zzq.utils.CacheUtils;

/**
 * 〈功能简述〉<br>
 * 〈项目启动初始化〉
 *
 * @create 2019/1/18
 */
@Component
public class ApplicationStartup implements ApplicationRunner {

    public static CacheUtils cacheUtils = null;

    @Autowired
    private CacheUtils cacheUtil;

    @Override
    public void run(ApplicationArguments args) {
        if(ApplicationStartup.cacheUtils == null){
            ApplicationStartup.cacheUtils = cacheUtil;
        }
    }
}
