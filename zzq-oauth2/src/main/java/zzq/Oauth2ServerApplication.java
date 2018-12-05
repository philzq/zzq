package zzq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 〈功能简述〉<br>
 * 〈授权中心〉
 *
 * @author zhouzhiqiang
 * @create 2018-12-05
 */
@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
public class Oauth2ServerApplication {

    public static void main(String[] args){
        SpringApplication.run(Oauth2ServerApplication.class,args);
    }
}
