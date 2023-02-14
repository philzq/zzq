package zzq.zzqsimpleframeworktlogclient.util;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * 〈功能简述〉<br>
 * 〈swagger工具了〉
 *
 * @create 2019/2/1
 */
public class Swagger2Utils {

    private static boolean swagger2Enabale = true;

    public static Docket api(String title, String version) {
        if (swagger2Enabale) {
            return new Docket(DocumentationType.SWAGGER_2)
                    .apiInfo(apiInfo(title, version))
                    .select()
                    //自行修改为自己的包路径
                    .apis(RequestHandlerSelectors.any())
                    .paths(PathSelectors.any())
                    .build();
        } else {//线上不展示swagger
            return new Docket(DocumentationType.SWAGGER_2)
                    .apiInfo(apiInfo(title, version))
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
