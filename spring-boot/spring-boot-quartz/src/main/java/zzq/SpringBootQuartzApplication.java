package zzq;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 〈功能简述〉<br>
 * 〈定时任务启动类〉
 *
 * @author zhouzhiqiang
 * @create 2018-12-09
 */
@SpringBootApplication
@MapperScan("zzq.mapper*")
public class SpringBootQuartzApplication {

    public static void main(String[] args){
        SpringApplication.run(SpringBootQuartzApplication.class,args);
    }
}
