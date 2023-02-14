## 一、日志组件封装
```xml
特点：
1.约束日志类型和日志参数
2.统一日志的使用规范
3.统一通用字段的收集
4.统一的日志通用的定制化处理（如组件info，error日志的收集等）
5.统一通用的日志配置，业务只需关注新增的业务日志
6.日志类型分为通用日志和业务日志，通用日志组件已经定义好了，使用方式已定义好，业务日志可自行定义，通过组件提供的LogUtilFactory使用即可
```

## 二、添加新的通用日志类型流程
```xml
1、zzq.zzqsimpleframeworklog.enums.LogTypeEnum 中添加枚举
2、zzq.zzqsimpleframeworklog.entity 中添加日志实体
3、zzq.zzqsimpleframeworklog.LogUtilFactory 定义日志工具类
4、src\main\resources\logback-spring.xml 定义日志文件
```
##### 以REMOTE-DIGEST为例
```xml
1、zzq.zzqsimpleframeworklog.enums.LogTypeEnum  
添加了REMOTE_DIGEST("remote.digest")枚举

2、zzq.zzqsimpleframeworklog.entity 
中添加RemoteDigestLogEntity日志实体

3、zzq.zzqsimpleframeworklog.LogUtilFactory 
定义日志工具类     LogAdvancedUtil<RemoteDigestLogEntity> REMOTE_DIGEST = LogUtilFactory.getLogUtil(LogTypeEnum.REMOTE_DIGEST, RemoteDigestLogEntity.class);

4、src\main\resources\logback-spring.xml 定义日志文件
    <appender name="REMOTE-DIGEST" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <File>${log.path}/${appName}-remote-digest.log</File>
        <rollingPolicy class="ch.qos.logback.core.rolling.FixedWindowRollingPolicy">
            <FileNamePattern>${log.path}/${appName}-remote-digest.log.%i</FileNamePattern>
            <MinIndex>1</MinIndex>
            <MaxIndex>12</MaxIndex>
        </rollingPolicy>
        <triggeringPolicy class="ch.qos.logback.core.rolling.SizeBasedTriggeringPolicy">
            <MaxFileSize>100MB</MaxFileSize>
        </triggeringPolicy>
        <layout class="ch.qos.logback.classic.PatternLayout">
            <Pattern>%msg%n</Pattern>
        </layout>
    </appender>

    <logger name="remote.digest" level="DEBUG" additivity="false">
        <appender-ref ref="REMOTE-DIGEST"/>
    </logger>
```


## 三、使用
具体用法可参考
zzq-simple-framework-log-client

1、添加依赖
```xml
        <dependency>
            <groupId>zzq</groupId>
            <artifactId>zzq-simple-framework-log</artifactId>
            <version>0.0.1-SNAPSHOT</version>
        </dependency> 
```

2、定义全局变量
```java
全局变量
System.setProperty("appName","zzq-simple-framework-log-client");
```
3、配置logback-spring.xml 
```xml
注：<include resource="logback-spring-common.xml"/>这行是引入组件里面的配置，必不可少
```
```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration>
    <include resource="logback-spring-common.xml"/>

    <appender name="TEST-LOG" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <File>${log.path}/${appName}-test-log.log</File>
        <rollingPolicy class="ch.qos.logback.core.rolling.FixedWindowRollingPolicy">
            <FileNamePattern>${log.path}/${appName}-test-log.log.%i</FileNamePattern>
            <MinIndex>1</MinIndex>
            <MaxIndex>12</MaxIndex>
        </rollingPolicy>
        <triggeringPolicy class="ch.qos.logback.core.rolling.SizeBasedTriggeringPolicy">
            <MaxFileSize>100MB</MaxFileSize>
        </triggeringPolicy>
        <layout class="ch.qos.logback.classic.PatternLayout">
            <Pattern>%msg%n</Pattern>
        </layout>
    </appender>

    <logger name="test.log" level="DEBUG" additivity="false">
        <appender-ref ref="TEST-LOG"/>
    </logger>
</configuration>
```
4、全局日志使用(如RPC日志，REMOTE日志，WEB日志)
```java
WebDigestLogEntity webDigestLogEntity = WebDigestLogEntity.builder()
        .build();
LogUtilFactory.WEB_DIGEST.info(webDigestLogEntity);
```
5、业务日志使用
```java
LogAdvancedUtil<TestLog> testLogLogAdvancedUtil = LogUtilFactory.getBusinessLogUtil("test.log", TestLog.class);
TestLog testLog = TestLog.builder()
        .test("测试呵呵呵哈哈哈哈哈哈哈")
        .build();
testLogLogAdvancedUtil.info(testLog);
```
6、业务中info与error日志的使用如下 （这么定义的目的是区分项目依赖组件打印的info与error日志）
```xml
        LogUtilFactory.SYSTEM_INFO.info("测试", "哦哦哦");

        LogUtilFactory.SYSTEM_ERROR.error("测试", "哦哦哦");
```

