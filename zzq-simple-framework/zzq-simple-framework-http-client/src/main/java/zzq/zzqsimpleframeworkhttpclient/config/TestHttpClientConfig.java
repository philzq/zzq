package zzq.zzqsimpleframeworkhttpclient.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import zzq.zzqsimpleframeworkhttp.config.HttpClient;
import zzq.zzqsimpleframeworkhttp.config.OkHttpClientProperties;

/**
 * 测试TestHttpClient配置
 * @author zhouzhiqiang
 * @version 1.0
 * @date 2023-01-06 11:24
 */
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
