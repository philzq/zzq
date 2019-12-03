package zzq.spring.boot.web.tomcat.container;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class SpringBootWebTomcatContainer {

    public static void main(String[] args){
        SpringApplication.run(SpringBootWebTomcatContainer.class,args);
    }

    @GetMapping("getTestStr")
    public String getTestStr(){
        return "Tomcat";
    }
}
