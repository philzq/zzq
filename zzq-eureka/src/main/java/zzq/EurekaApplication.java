package zzq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * 〈功能简述〉<br>
 * 〈服务注册中心启动类〉
 *
 * @author zhouzhiqiang
 * @create 2018/11/17 0017
 */
@EnableEurekaServer  //启动一个服务注册中心提供给其他应用进行对话
@SpringBootApplication
public class EurekaApplication {

    public static void main(String[] args){
        SpringApplication.run(EurekaApplication.class,args);
    }
}
