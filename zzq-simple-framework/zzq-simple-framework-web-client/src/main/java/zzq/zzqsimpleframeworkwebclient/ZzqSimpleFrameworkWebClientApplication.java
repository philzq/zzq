package zzq.zzqsimpleframeworkwebclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import zzq.zzqsimpleframeworkcommon.util.ProjectUtil;

/**
 * @author zhouzhiqiang
 * @version 1.0
 * @date 2023-01-06 11:22
 */
@SpringBootApplication(scanBasePackages = "zzq")
public class ZzqSimpleFrameworkWebClientApplication {

    public static void main(String[] args) {
        ProjectUtil.startInit("zzq-simple-framework-web-client");
        SpringApplication.run(ZzqSimpleFrameworkWebClientApplication.class, args);
    }
}
