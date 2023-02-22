## 一、组件描述
web项目通用功能封装
## 二、使用教程
demo项目参考：zzq-simple-framework-web-client

1、添加依赖
```
        <dependency>
            <groupId>zzq</groupId>
            <artifactId>zzq-simple-framework-web</artifactId>
            <version>0.0.1-SNAPSHOT</version>
        </dependency> 
```
2、启动类扫描路径一定要加com.kdniao
```
@SpringBootApplication(scanBasePackages = "com.kdniao")
```
3、kafka生产者要指定生产拦截器
```
spring:
  kafka:
    producer:
      properties:
        interceptor.classes: com.kdniao.general.common.web.kafka.KafkaProducerContextInterceptor
```

## 三、版本迭代内容
1.0.1
```
1、xxljob全局日志收集及上下文信息传递
2、web全局日志收集及上下文信息传递
3、kafka的Producer和Consumer全局日志收集及上下文信息传递
4、web项目全局异常捕获处理
```



