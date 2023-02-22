package zzq.zzqsimpleframeworkcacheclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import zzq.simple.framework.cache.config.EnableCaffeineCache;

/**
 * @author zhouzhiqiang
 * @version 1.0
 * @date 2023-01-03 17:04
 */
@SpringBootApplication
@EnableCaffeineCache
public class ZzqSimpleFrameWorkCacheClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZzqSimpleFrameWorkCacheClientApplication.class, args);
    }
}
