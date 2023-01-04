## 官方网站
id|desc|url
---|----|----
1|jackson|https://github.com/FasterXML/jackson
2|gson谷歌的|https://www.baeldung.com/jackson-vs-gson
3|fastJson|https://github.com/alibaba/fastjson

## 使用教程
测试demo查看zzq-simple-framework-json-client，测试数据
```
{
  "aboolean": true,
  "adouble": 0,
  "localDate": "2023-01-04",
  "localDateTime": "2023-01-04 06:24:39",
  "localDateTime2": "2023-01-04 06:31:34.713",
  "localTime": "06:24:39",
  "look": {
    "aboolean": true,
    "adouble": 0,
    "localDate": "2023-01-04",
    "localDateTime": "2023-01-04 06:24:39",
    "localTime": "06:24:39",
    "str": "string"
  },
  "str": "string"
}
```
```java
一、开启注解@EnableJackson
@SpringBootApplication
@EnableJackson
public class ZzqSimpleFrameworkJsonClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZzqSimpleFrameworkJsonClientApplication.class, args);
    }
}
二、使用
@Autowired
private JacksonUtil jacksonUtil;

@PostMapping("jsonTestEntity")
private JsonTestEntity jsonTestEntity(@RequestBody JsonTestEntity jsonTestEntity){

    String toJSon = jacksonUtil.toJSon(jsonTestEntity);

    System.out.println("toJSon:\n" + toJSon);

    JsonTestEntity parseJson = jacksonUtil.parseJson(toJSon, new TypeReference<JsonTestEntity>() {
    });

    return parseJson;
}
三、自定义日期格式
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



