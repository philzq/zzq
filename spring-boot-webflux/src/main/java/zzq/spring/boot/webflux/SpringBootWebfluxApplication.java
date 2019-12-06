package zzq.spring.boot.webflux;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@SpringBootApplication
@RestController
@EnableEurekaClient
public class SpringBootWebfluxApplication {

    public static void main(String[] args){
        SpringApplication.run(SpringBootWebfluxApplication.class,args);
    }

    @GetMapping("/getTestStr")
    public Mono<String> getTestStr() throws Exception{   // 【改】返回类型为Mono<String>
        Thread.sleep(50);
        return Mono.just("Welcome to reactive world ~");     // 【改】使用Mono.just生成响应式数据
    }
}
