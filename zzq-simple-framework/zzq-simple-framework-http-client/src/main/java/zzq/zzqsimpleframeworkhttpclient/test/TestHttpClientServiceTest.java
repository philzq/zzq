package zzq.zzqsimpleframeworkhttpclient.test;

import com.fasterxml.jackson.core.type.TypeReference;
import okhttp3.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import zzq.zzqsimpleframeworkhttp.config.HttpClient;
import zzq.zzqsimpleframeworkhttp.config.OkHttpClientProperties;

import javax.annotation.PostConstruct;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author zhouzhiqiang
 * @version 1.0
 * @date 2023-01-06 11:30
 */
@Service
public class TestHttpClientServiceTest {
    
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
}
