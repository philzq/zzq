package zzq.zzqsimpleframeworkhttp.config;

import okhttp3.*;
import okhttp3.logging.HttpLoggingInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.Duration;


public class HttpClient {

    private final static Logger logger = LoggerFactory.getLogger(LogEntity.class);

    public OkHttpClient OkHttpClient;

    public HttpClient() {
        Dispatcher dispatcher = new Dispatcher();
        dispatcher.setMaxRequests(64);
        dispatcher.setMaxRequestsPerHost(5);
        dispatcher.setIdleCallback(new Runnable() {
            @Override
            public void run() {
                LogEntity.printfFinallyLog();
            }
        });

        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(content -> LogEntity.collectLogWithTimeCycle(content, false));
        httpLoggingInterceptor.level(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient = new OkHttpClient().newBuilder()
                .connectionPool(new ConnectionPool()) // 使用默认配置 https://github.com/square/okhttp/blob/master/okhttp-testing-support/src/main/java/okhttp3/TestUtil.java#L44
                .dispatcher(dispatcher)
                .addInterceptor(httpLoggingInterceptor)
                .eventListener(new CustomEventListener())
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
