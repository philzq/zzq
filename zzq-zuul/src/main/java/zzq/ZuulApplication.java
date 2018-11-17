package zzq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * 〈功能简述〉<br>
 * 〈网关启动类〉
 *
 * @author zhouzhiqiang
 * @create 2018/11/17 0017
 */
@SpringBootApplication
@EnableZuulProxy
public class ZuulApplication {
    
    public static void main(String[] args){
        SpringApplication.run(ZuulApplication.class,args);
    }
}
