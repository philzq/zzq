package zzq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zzq.entity.User;
import zzq.rpc.UserService;
import zzq.utils.R;

/**
 * 〈功能简述〉<br>
 * 〈网关启动类〉
 *
 * @author zhouzhiqiang
 * @create 2018/11/17 0017
 */
@SpringBootApplication
@EnableEurekaServer
@EnableEurekaClient
@EnableFeignClients
@RestController
public class ZuulApplication {

    @Autowired
    UserService us;

    @RequestMapping("findOne")
    public R findOne(User user){
        return us.findOne(user);
    }
    
    public static void main(String[] args){
        SpringApplication.run(ZuulApplication.class,args);
    }
}
