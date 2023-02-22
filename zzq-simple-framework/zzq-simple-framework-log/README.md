## 一、组件描述
log日志组件的封装

## 二、添加新的通用日志类型流程
```
1、zzq.zzqsimpleframeworklog.enums.LogTypeEnum 中添加枚举
2、zzq.zzqsimpleframeworklog.entity 中添加日志实体
3、zzq.zzqsimpleframeworklog.LogUtilFactory 定义日志工具类
4、src\main\resources\logback-spring.xml 定义日志文件
```
##### 以REMOTE-DIGEST为例
```
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
demo教程：zzq-simple-framework-log-client

1、添加依赖
```
        <dependency>
            <groupId>zzq</groupId>
            <artifactId>zzq-simple-framework-log</artifactId>
            <version>0.0.1-SNAPSHOT</version>
        </dependency> 
```

2、定义全局变量
```
全局变量
System.setProperty("appName","zzq-simple-framework-json-client");
```
3、配置logback-spring.xml 
```
注：<include resource="logback-spring-common.xml"/>这行是引入组件里面的配置，必不可少
```
```
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
```
WebDigestLogEntity webDigestLogEntity = WebDigestLogEntity.builder()
        .build();
LogUtilFactory.WEB_DIGEST.info(webDigestLogEntity);
```
5、业务日志使用
```
LogAdvancedUtil<TestLog> testLogLogAdvancedUtil = LogUtilFactory.getBusinessLogUtil("test.log", TestLog.class);
TestLog testLog = TestLog.builder()
        .test("测试呵呵呵哈哈哈哈哈哈哈")
        .build();
testLogLogAdvancedUtil.info(testLog);
```
6、业务中info与error日志的使用如下 （这么定义的目的是区分项目依赖组件打印的info与error日志）
```
        LogUtilFactory.SYSTEM_INFO.info("测试", "哦哦哦");

        LogUtilFactory.SYSTEM_ERROR.error("测试", "哦哦哦");
```

## 四、版本迭代内容
0.0.1-SNAPSHOT
```
1、封装了下述的通用日志及使用
SYSTEM_INFO("system.info"),//业务info日志
SYSTEM_ERROR("system.error"),//业务error日志
REMOTE_DIGEST("remote.digest"),//远程调用日志
RPC_DIGEST("rpc.digest"),//rpc调用日志
MONGO("mongo"),//mongo 语句日志
WEB_DIGEST("web.digest"),//web日志
KAFKA("kafka"),//kafka生产日志
TASK("task"),//TASK日志
2、封装了日志基类，约束日志的结构，及日志全局属性的统一赋值
3、基于CustomInfoMessageConverter，CustomErrorMessageConverter，封装默认的info日志和error输出的日志内容
4、基于CustomErrorLayout处理error日志输出无结构的堆栈信息
5、基于CustomLevelFilter自定义日志输出逻辑
6、提供LogAdvancedUtil、LogErrorBasicUtil、LogInfoBasicUtil日志工具类及LogUtilFactory统一日志的使用入口
```

