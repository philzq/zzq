package zzq.zzqsimpleframeworkjsonclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import zzq.zzqsimpleframeworkjson.config.EnableJacksonConfigure;

/**
 * @author zhouzhiqiang
 * @version 1.0
 * @date 2023-01-04 14:06
 */
@SpringBootApplication
@EnableJacksonConfigure
public class ZzqSimpleFrameworkJsonClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZzqSimpleFrameworkJsonClientApplication.class, args);
    }
}
