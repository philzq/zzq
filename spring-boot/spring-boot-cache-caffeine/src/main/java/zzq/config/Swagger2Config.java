package zzq.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

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
        return api("zzq.controller","zzq-admin-API文档","0.0.1-SNAPSHOT");
    }

    private static boolean swagger2Enabale = true;

    public static Docket api(String packagePath,String title,String version) {
        if(swagger2Enabale){
            return new Docket(DocumentationType.SWAGGER_2)
                    .apiInfo(apiInfo(title,version))
                    .select()
                    //自行修改为自己的包路径
                    .apis(RequestHandlerSelectors.basePackage(packagePath))
                    .paths(PathSelectors.any())
                    .build();
        }else{//线上不展示swagger
            return new Docket(DocumentationType.SWAGGER_2)
                    .apiInfo(apiInfo(title,version))
                    .select()
                    //自行修改为自己的包路径
                    .apis(RequestHandlerSelectors.basePackage("abcdefg.gfedcba"))
                    .paths(PathSelectors.any())
                    .build();
        }
    }

    private static ApiInfo apiInfo(String title, String version) {
        return new ApiInfoBuilder()
                //页面标题
                .title(title)
                //版本号
                .version(version)
                .build();
    }
}

