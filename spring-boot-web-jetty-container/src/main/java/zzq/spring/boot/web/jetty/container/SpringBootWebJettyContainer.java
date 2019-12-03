package zzq.spring.boot.web.jetty.container;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class SpringBootWebJettyContainer {

    public static void main(String[] args){
        SpringApplication.run(SpringBootWebJettyContainer.class,args);
    }

    @GetMapping("getTestStr")
    public String getTestStr() throws Exception{
        Thread.sleep(500);
        return "Tomcat";
    }
}
