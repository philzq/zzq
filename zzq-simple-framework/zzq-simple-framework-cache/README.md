# 该项目用于对本地缓存Caffeine和分布式缓存redis的封装
## 一、Caffeine
#### 为什么有Caffeine
spring-boot-cache默认不支持LoadingCache，但我们希望的是数据还没过期时调用当前方法刷一份新的就行，但缓存默认没有用aop所以实现不了？


#### 实现方法
使用aop实现，具体代码：[**自定义CacheManager**](src/main/java/yongfa365/config/CaffeineConfig.java)

没有使用默认配置，直接用就行了

```xml
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-aop</artifactId>
</dependency>
<!-- https://mvnrepository.com/artifact/com.github.ben-manes.caffeine/caffeine -->
<dependency>
	<groupId>com.github.ben-manes.caffeine</groupId>
	<artifactId>caffeine</artifactId>
	<version>3.1.2</version>
</dependency>
```
```java
//spring-boot默认加了spring.aop.auto: true，所以这句不是必须的
@EnableAspectJAutoProxy
```

```java
//CacheName==@CacheableLoading的所有属性拼成的字符串，相同的CacheName会放在同一个LoadingCache里。
//CacheKey=方法完整签名+参数值，一个LoadingCache里有多个CacheKey及对应的缓存内容
//同一方法因参数不同会产生多个CacheKey
//不止要设置刷新时间，还应同时设置过期时间等，否则可能越积越多。过期时间是固定的，刷新不影响他。
@CacheableLoading(expireAfterWrite = 20, refreshAfterWrite = 5, maximumSize=1000, timeout=5)
String getDataWithCaffeineLoadingCache(String input) {...}
```

#### Caffeine核心参数介绍
```
name : 缓存名称，可不写不重要
maximumSize: 缓存的最大数量
expireAfterAccess: 最后一次读或写操作后经过指定时间过期
expireAfterWrite: 最后一次写操作后经过指定时间过期
refreshAfterWrite: 创建缓存或者最近一次更新缓存后经过指定时间间隔，刷新缓存
recordStats：开发统计功能
timeout : 出错后要等几秒再尝试，默认10秒，防止出错后不停的发起穿透请求。
```

## 二、redis


