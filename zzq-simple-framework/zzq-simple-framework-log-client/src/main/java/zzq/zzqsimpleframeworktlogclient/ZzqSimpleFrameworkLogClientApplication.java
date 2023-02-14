package zzq.zzqsimpleframeworktlogclient;

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
public class ZzqSimpleFrameworkLogClientApplication {

    public static void main(String[] args) {
        System.setProperty("appName", "zzq-simple-framework-log-client");
        SpringApplication.run(ZzqSimpleFrameworkLogClientApplication.class, args);
    }
}
