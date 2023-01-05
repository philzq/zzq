package zzq.zzqsimpleframeworkhttp.config;

import okhttp3.*;

import java.io.IOException;
import java.time.Duration;


public class HttpClient {

    public OkHttpClient OkHttpClient;

    public HttpClient() {
        CustomEventListener eventListener = new CustomEventListener();
        OkHttpClient = new OkHttpClient().newBuilder()
                .connectionPool(new ConnectionPool()) // 使用默认配置 https://github.com/square/okhttp/blob/master/okhttp-testing-support/src/main/java/okhttp3/TestUtil.java#L44
                .addInterceptor(HttpLoggingInterceptors.customLoggingInterceptor)
                .addInterceptor(HttpLoggingInterceptors.httpLoggingInterceptor)
                .eventListener(eventListener)
                .connectTimeout(Duration.ofSeconds(10))// default 10s
                .readTimeout(Duration.ofSeconds(10))// default 10s
                .writeTimeout(Duration.ofSeconds(10))// default 10s
                .sslSocketFactory(IgnoreTsl.SOCKET_FACTORY, IgnoreTsl.TRUST_ALL_MANAGER)
                .hostnameVerifier((hostname, session) -> true)
                .retryOnConnectionFailure(true)
                .build();
    }

    public String body(Response response) {
        try {
            return response.body() != null ? response.body().string() : null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
