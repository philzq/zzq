package zzq.zzqsimpleframeworkcacheclient.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zzq.simple.framework.cache.caffeine.config.CacheableLoading;
import zzq.zzqsimpleframeworkcacheclient.common.Helper;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;

@Slf4j
@Service
public class MyService {

    @Autowired
    MyService myService;

    @CacheableLoading(name = "可以不要", expireAfterWrite = 100, refreshAfterWrite = 50, maximumSize = 10, recordStats = true)
    String getDataWithCaffeineLoadingCache(String input) {

        log.info("穿透getDataWithCaffeineLoadingCache {} ", input);
        return String.format("input:%s , data:%s", input, LocalDateTime.now().getSecond());
    }

    public static int count = 0;

    @CacheableLoading(name = "可以不要", expireAfterWrite = 20, refreshAfterWrite = 5, maximumSize = 1000, recordStats = true, timeout = 5)
    String getDataWithCaffeineLoadingCacheWithException(String input) throws Exception {
        if (count++ < 1 || count > 5) {
            return String.format("input:%s , data:%s", input, LocalDateTime.now().getSecond());
        }

        throw new Exception("出错了");

    }

    @CacheableLoading(name = "缓存过期后等待，不刷新", expireAfterWrite = 3)
    String getDataWithCaffeineNoLoadingCache(String input) {
        log.info("穿透getDataWithCaffeineNoLoadingCache{} Start", input);
        log.info("穿透getDataWithCaffeineNoLoadingCache {} End", input);
        return String.format("input:%s , data:%s", input, LocalDateTime.now().getSecond());
    }

    @CacheableLoading(expireAfterAccess = 3, refreshAfterWrite = 2)
    String expireAfterAccess(String input) {
        log.info("穿透expireAfterAccess {}", input);
        return String.format("input:%s , data:%s", input, LocalDateTime.now().getSecond());
    }

    String getData2(String input) {
        log.info("穿透getData2 {} Start", input);
        Helper.sleep(5000);
        log.info("穿透getData2 {} End", input);
        return String.format("input:%s , data:%s", input, LocalDateTime.now().getSecond());
    }

    @PostConstruct
    public void test(){
        //测试过期，缓存不刷新
        /*for(int i=0;i<5;i++){
            myService.getDataWithCaffeineNoLoadingCache("测试缓存不刷新----出现五次即通过");
            Helper.sleep(3001);
        }*/

        //测试过期，缓存刷新
        for(int i=0;i<50;i++){
            String expireAfterAccess = myService.getDataWithCaffeineLoadingCache("测试缓存刷新----自动刷新，调用走缓存，看日志间隔时间"+(i%25));
            log.info(expireAfterAccess);
        }



    }

}
