## 一、组件描述
基于Caffeine的本地缓存

## 二、使用教程
demo教程：zzq-simple-framework-cache-client

1、添加依赖
```
        <dependency>
          <groupId>zzq</groupId>
          <artifactId>zzq-simple-framework-cache</artifactId>
          <version>0.0.1-SNAPSHOT</version>
        </dependency>
```
2、使用@EnableCaffeineCache注解开启CaffeineCache
```
@SpringBootApplication
@EnableCaffeineCache
public class ZzqSimpleFrameWorkCacheClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZzqSimpleFrameWorkCacheClientApplication.class, args);
    }
}
```
3、使用注解CacheableLoading使用缓存
```
@CacheableLoading(name = "可以不要", expireAfterWrite = 100, refreshAfterWrite = 50, maximumSize = 10, recordStats = true)
String getDataWithCaffeineLoadingCache(String input) {

    log.info("穿透getDataWithCaffeineLoadingCache {} ", input);
    return String.format("input:%s , data:%s", input, LocalDateTime.now().getSecond());
}
```
Caffeine核心参数介绍
```
  name : 缓存名称，可不写不重要
  maximumSize: 缓存的最大数量
  expireAfterAccess: 最后一次读或写操作后经过指定时间过期
  expireAfterWrite: 最后一次写操作后经过指定时间过期
  refreshAfterWrite: 创建缓存或者最近一次更新缓存后经过指定时间间隔，刷新缓存
  recordStats：开发统计功能
  timeout : 出错后要等几秒再尝试，默认10秒，防止出错后不停的发起穿透请求。
```

## 三、版本迭代内容
1.0.1
```
1.基于aop实现的本地缓存
```

