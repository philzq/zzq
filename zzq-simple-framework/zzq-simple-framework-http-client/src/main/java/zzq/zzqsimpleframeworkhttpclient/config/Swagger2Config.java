package zzq.zzqsimpleframeworkhttpclient.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import zzq.zzqsimpleframeworkhttpclient.util.Swagger2Utils;

/**
 * 〈功能简述〉<br>
 * 〈swaggerConfig〉
 *
 * @author zhouzhiqiang
 * @create 2018-12-04
 */
@Configuration
@EnableSwagger2
public class Swagger2Config {

    @Bean
    public Docket api() {
        return Swagger2Utils.api("zzq.zzqsimpleframeworkhttpclient.controller","ZzqSimpleFrameworkHttpClientApplication-API文档","0.0.1-SNAPSHOT");
    }
}

