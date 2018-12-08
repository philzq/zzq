package zzq;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * 〈功能简述〉<br>
 * 〈应用中心监控〉
 *
 * @author zhouzhiqiang
 * @create 2018-12-08
 */
@EnableAdminServer
@SpringBootApplication
@EnableEurekaClient
public class SpringBootAdminApplication {
    
    public static void main(String[] args){
        SpringApplication.run(SpringBootAdminApplication.class,args);
    }
}
