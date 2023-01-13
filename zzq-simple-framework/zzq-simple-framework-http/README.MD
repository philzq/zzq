## 主流HttpClient框架比较
- 主要研究过JDK及OkHttp，以下对HttpComponents的评价可能不够客观。
- google反对HttpComponents因设计复杂，而认可OkHttp且默认继承到android。
- 大家都认可google，所以选OkHttp不会错。
- 虽然现在[排行第二](https://mvnrepository.com/open-source/http-clients)，但他比第一出来晚多了，足见大势所趋。

|Feature|[JDK HttpClient](https://openjdk.java.net/groups/net/httpclient/intro.html)|[Apache HttpClient](http://hc.apache.org/)|[OkHttp](https://square.github.io/okhttp/)|
|----|----|----|----|
|Supporter|Oracle|社区(springboot集成了它))|Android默认集成(被google认可)|
|诞生时间|2018年|2007年前|2013年|
|活跃度|<a title="3年一个稳定版">☆</a>|<a title="1年2-4次">☆☆☆</a>|<a title="1月1-2次">☆☆☆☆☆☆</a>|
|文档|☆☆|☆☆☆|☆☆☆☆☆|
|Compress(gzip)|☆☆|☆☆☆☆|☆☆☆☆☆|
|MultiPart|✘|✔|✔|
|UrlBuilder|✘|✔|✔|
|Intercept|✘|✔|✔✔✔|
|Event|✘|✘|✔✔✔|
|日志详细度|--|--|<a title="dns解析，连接，发送，接收时间全都有，很为企业级用户考虑">☆☆☆☆☆</a>|
|依赖外部|0|2个共1300K|4个共800K|
|maven引用数|--|89K|48K|
|GitHub Star|--|1K主要没在这维护|35K|

## 测试http请求、证书几个好网站：
https://httpbin.org/status/503
https://badssl.com/
http://httpstat.us/502


# 技术选型OKHTTP
## OKHTTP官方网站
|id|desc|url|
|---|----|----|
|1|官网|https://square.github.io/okhttp/|
|2|官方例子|https://github.com/square/okhttp/tree/master/samples/guide/src/main/java/okhttp3/recipes|


## 使用方法
一、配置OkHttpClientProperties和HttpClient
```yaml
httpclient:
    pool:
      test:
        maxIdleConnections: 100
        maxRequests: 100    #只对异步请求生效
        maxRequestsPerHost: 100    #只对异步请求生效
        connectTimeout: 10
        readTimeout: 10
        writeTimeout: 10
```
```java
@Configuration
public class TestHttpClientConfig {
    
    @ConfigurationProperties("httpclient.pool.test")
    @Bean("testOkHttpClientProperties")
    public OkHttpClientProperties okHttpClientProperties(){
        return OkHttpClientProperties.builder().build();
    }

    @Bean("testHttpClient")
    public HttpClient httpClient(@Qualifier("testOkHttpClientProperties") OkHttpClientProperties okHttpClientProperties){
        return new HttpClient(okHttpClientProperties);
    }
}
```

二、使用
```java
    @Autowired
    @Qualifier("testHttpClient")
    private HttpClient httpClient;
    
    @PostConstruct
    public void test(){
        //这里写了一堆url，只是便于测试，最终只会用最后一个，自行注释掉其他的来测试
        /*Request request = new Request.Builder()
                .url("http://asdfasdfasdfasfe333333.com/") //可以测试dns问题
                //.url("https://self-signed.badssl.com/") //可以用原生的okhttpclient测试假证书问题
                //.url("http://httpstat.us/500?sleep=5000") //测试延时
                //.url("http://httpstat.us/404")
                //.url("http://httpstat.us/502")
                .url("https://cdn.sstatic.net/Sites/stackoverflow/img/favicon.ico?v=4f32ecc8f43d")
                .url("https://www.baidu.com")
                .url("http://httpstat.us/200")
                .header("Accept", "application/json")  //测试httpstat.us时需要加这个，不然获取到的body是空
                .header("Accept-Encoding", "gzip, deflate")
                .build();*/

        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 90; i++) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    //只要是Server响应了，就会有Response，包括：400,403,404,500,502,503等
                    String response = httpClient.post("http://httpstat.us/200", new TypeReference<String>() {
                    });
                    //httpClient.getOkHttpClient().newCall(request).cancel();
                }
            });
        }
    }

 ```

三、使用newBuilder基于原有OkHttpClient自定义配置，如代理
 ```java
httpClient.getOkHttpClient().newBuilder().proxy(null).build();
 ```

## 注意
如果是响应是json结构的请求
 ```java
Object response = httpClient.post("http://httpstat.us/200", new TypeReference<Object>() {
                    });
 ```
如果不是json结构的请求
```java
String response = httpClient.post("http://httpstat.us/200");
 ```
 
