package zzq.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import zzq.utils.RedisUtils;

/**
 * 〈功能简述〉<br>
 * 〈项目启动初始化〉
 *
 * @create 2019/1/18
 */
@Component
public class ApplicationStartup implements ApplicationRunner {

    public static RedisUtils redisUtils = null;

    @Autowired
    private RedisUtils cacheUtil;

    @Override
    public void run(ApplicationArguments args) {
        if(ApplicationStartup.redisUtils == null){
            ApplicationStartup.redisUtils = cacheUtil;
        }
    }
}
