package zzq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import zzq.utils.CacheUtils;

/**
 * 〈功能简述〉<br>
 * 〈springbootwebsocket启动类〉
 *
 * @author zhouzhiqiang
 * @create 2018-12-21
 */
@SpringBootApplication
public class SpringBootWebsocketApplication {
    
    public static void main(String[] args){
        SpringApplication.run(SpringBootWebsocketApplication.class,args);
    }

    @Bean
    @Lazy
    public CacheUtils cacheUtils(){
        return new CacheUtils();
    }
}

