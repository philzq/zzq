## 一、组件特点
基于jackson的json使用

## 二、使用教程
demo教程：zzq-simple-framework-json-client

1、添加依赖
```
        <dependency>
            <groupId>zzq</groupId>
            <artifactId>zzq-simple-framework-json</artifactId>
            <version>0.0.1-SNAPSHOT</version>
        </dependency>
```
2、开启注解@EnableJackson，全局设置spring的json序列化与反序列化
```
@SpringBootApplication
@EnableJacksonConfigure
public class ZzqSimpleFrameworkJsonClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZzqSimpleFrameworkJsonClientApplication.class, args);
    }
}
```
3、JacksonUtil使用
```
@PostMapping("jsonTestEntity")
private JsonTestEntity jsonTestEntity(@RequestBody JsonTestEntity jsonTestEntity){

    String toJSon = JacksonUtil.toJSon(jsonTestEntity);

    System.out.println("toJSon:\n" + toJSon);

    JsonTestEntity parseJson = JacksonUtil.parseJson(toJSon, new TypeReference<JsonTestEntity>() {
    });

    return parseJson;
}
```
4、自定义日期格式
```
// JacksonConfigure 有统一处理日期的格式，如果想自定义可以在字段的getXXX上单独指定
public class OrderLog {
    private LocalDateTime operateTime;

    // 只是对
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    public LocalDateTime getOperateTime() {
        return operateTime;
    }
}
```
## 三、版本迭代内容
1.0.1
```
1.全局统一json序列化与反序列化的方式
2.提供JacksonUtil工具类统一json的使用
```



