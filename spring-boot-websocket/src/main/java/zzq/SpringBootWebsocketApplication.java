package zzq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import zzq.annotation.EnableRedisUtils;
import zzq.utils.RedisUtils;

/**
 * 〈功能简述〉<br>
 * 〈springbootwebsocket启动类〉
 *
 * @author zhouzhiqiang
 * @create 2018-12-21
 */
@SpringBootApplication
@EnableRedisUtils
public class SpringBootWebsocketApplication {
    
    public static void main(String[] args){
        SpringApplication.run(SpringBootWebsocketApplication.class,args);
    }
}

