package zzq.spring.boot.webflux;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.WebFilter;
import reactor.core.publisher.Mono;

@SpringBootApplication
@RestController
@EnableEurekaClient
public class SpringBootWebfluxApplication {

    @Autowired
    private ServerProperties serverProperties;

    public static void main(String[] args){
        SpringApplication.run(SpringBootWebfluxApplication.class,args);
    }

    @Bean
    public WebFilter contextPathWebFilter() {
        String contextPath = serverProperties.getServlet().getContextPath();
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            if (request.getURI().getPath().startsWith(contextPath)) {
                return chain.filter(
                        exchange.mutate()
                                .request(request.mutate().contextPath(contextPath).build())
                                .build());
            }
            return chain.filter(exchange);
        };
    }

    @GetMapping("/getTestStr")
    public Mono<String> getTestStr() throws Exception{   // 【改】返回类型为Mono<String>
        Thread.sleep(3000);
        return Mono.just("Welcome to reactive world ~");     // 【改】使用Mono.just生成响应式数据
    }
}
