package zzq.spring.boot.web.container;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class SpringBootWebContainerApplication {

    public static void main(String[] args){
        SpringApplication.run(SpringBootWebContainerApplication.class,args);
    }

    @GetMapping("getTestStr")
    public String getTestStr(){
        return "Undertow";
    }
}
