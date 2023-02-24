package zzq.zzqsimpleframeworkhttp;

import feign.Feign;
import feign.RequestLine;
import zzq.zzqsimpleframeworkhttp.config.OkHttpFeignClient;

/**
 * @author zhouzhiqiang
 * @version 1.0
 * @date 2023-02-22 17:36
 */
public class FeignClientDemo {

    public static void main(String[] args) {
        Hello target = Feign.builder()
                .client(new OkHttpFeignClient(100))
                .target(Hello.class, "http://httpstat.us/");
        String name = target.hello();
        System.out.println(name);
    }

    interface Hello {

        @RequestLine(value = "POST /200")
        String hello();
    }
}
