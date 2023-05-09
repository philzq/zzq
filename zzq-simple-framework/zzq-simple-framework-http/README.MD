## 一、组件描述
基于okhttp的http组件


## 二、使用方法
demo教程：zzq-simple-framework-http-client

#### a、 HttpClient使用教程

1、添加依赖
```
        <dependency>
            <groupId>zzq</groupId>
            <artifactId>zzq-simple-framework-http</artifactId>
            <version>0.0.1-SNAPSHOT</version>
        </dependency>
```

2、配置HttpClientUtil
```
public class HttpClientUtils {

    public static final HttpClient testHttpClient = new HttpClient("http://httpstat.us/", 100);


}
```

3、使用
```
    public String getOK() {
        Map<String, String> header = new HashMap<>();
        header.put("head1", "head1");
        header.put("head2", "head2");
        header.put("head3", "head3");
        Map<String, String> params = new HashMap<>();
        params.put("param1", "param1");
        params.put("param2", "param2");
        params.put("param3", "param3");
        //只要是Server响应了，就会有Response，包括：400,403,404,500,502,503等
        String response = HttpClientUtils.testHttpClient.get("200?time=" + System.currentTimeMillis(), params, header);
        return response;
    }
 ```

4、使用newBuilder基于原有OkHttpClient自定义配置，如代理
 ```
OkHttpClient okHttpClient = httpClient.getOkHttpClient().newBuilder().proxy(null).build();
HttpClient newHttpClient = httpClient.getNewHttpClient(okHttpClient);
 ```

#### 注意
如果是响应是json结构的请求
 ```
Object response = httpClient.post("200", new TypeReference<Object>() {
                    });
 ```
如果不是json结构的请求
```
String response = httpClient.post("200");
 ```

#### b、 FeignClient使用教程
1、不基于@FeignClient注解，直接调用的使用方式
 ```
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
 ```
 
 2、基于@FeignClient注解使用方式
 
 注入client
  ```
 @Configuration
 public class TestFeignCustomClientConfig {
 
     @Bean("userFacadeOkHttpFeignClient")
     public OkHttpFeignClient testOkHttpFeignClient(LoadBalancerClient loadBalancerClient, LoadBalancerClientFactory loadBalancerClientFactory){
         OkHttpFeignClient okHttpFeignClient = new OkHttpFeignClient(100);
         okHttpFeignClient.setLoadBalancerClient(loadBalancerClient);
         okHttpFeignClient.setLoadBalancerClientFactory(loadBalancerClientFactory);
         return okHttpFeignClient;
     }  
 }
  ```
 配置client
  ```
 @FeignClient(name = "user-api", contextId = "userFacadeOkHttpFeignClient")
  ```
 
## 三、版本迭代内容
0.0.1-SNAPSHOT
```
1、基于RemoteDigestLogEntity日志实体，Request的tag，全面收集http请求的全链路日志
2、封装HttpClient工具类，简化及统一http功能的使用
3、集成feignclient与okhttpclient,feignclient集成注册中心
```
