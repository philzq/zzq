package zzq.zzqsimpleframeworkhttp.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * okhttpclient配置
 * @author zhouzhiqiang
 * @version 1.0
 * @date 2023-01-05 14:27
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OkHttpClientProperties {

    @Builder.Default
    private int maxIdleConnections = 200; // 最大连接数

    @Builder.Default
    private int maxRequests = 200;// 最大请求数据

    @Builder.Default
    private int maxRequestsPerHost = 50; // 每台主机的最大并发数

    @Builder.Default
    private long connectTimeout = 10; // 链接超时时间，单位s

    @Builder.Default
    private long readTimeout = 10; //读超时时间,单位s

    @Builder.Default
    private long writeTimeout = 10; //写超时时间，单位s
}
