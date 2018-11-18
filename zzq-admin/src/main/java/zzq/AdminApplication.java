package zzq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 〈功能简述〉<br>
 * 〈admin启动类〉
 *
 * @author zhouzhiqiang
 * @create 2018/11/17 0017
 */
@SpringBootApplication
@EnableEurekaClient
@EnableTransactionManagement
public class AdminApplication {

    public static void main(String[] args){
        SpringApplication.run(AdminApplication.class,args);
    }
}
