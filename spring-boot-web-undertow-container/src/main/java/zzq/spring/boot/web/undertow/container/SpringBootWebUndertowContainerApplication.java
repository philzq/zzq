package zzq.spring.boot.web.undertow.container;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class SpringBootWebUndertowContainerApplication {

    public static void main(String[] args){
        SpringApplication.run(SpringBootWebUndertowContainerApplication.class,args);
    }

    @GetMapping("getTestStr")
    public String getTestStr() throws Exception{
        Thread.sleep(50);
        return "Undertow";
    }
}
