package zzq.zzqsimpleframeworkhttp.config;

import okhttp3.ConnectionPool;
import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

import java.time.Duration;
import java.util.concurrent.TimeUnit;


public class HttpClient {

    private OkHttpClientProperties okHttpClientProperties;

    private OkHttpClient OkHttpClient;

    public HttpClient(OkHttpClientProperties okHttpClientProperties) {
        Dispatcher dispatcher = new Dispatcher();
        dispatcher.setMaxRequests(okHttpClientProperties.getMaxRequests());
        dispatcher.setMaxRequestsPerHost(okHttpClientProperties.getMaxRequestsPerHost());
        dispatcher.setIdleCallback(new Runnable() {
            @Override
            public void run() {
                LogEntity.printfFinallyLog();
            }
        });

        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(content -> LogEntity.collectLogWithTimeCycle(content, false));
        httpLoggingInterceptor.level(HttpLoggingInterceptor.Level.BODY);

        ConnectionPool connectionPool = new ConnectionPool(okHttpClientProperties.getMaxIdleConnections(),15, TimeUnit.SECONDS);

        OkHttpClient = new OkHttpClient().newBuilder()
                .connectionPool(connectionPool) // 使用默认配置 https://github.com/square/okhttp/blob/master/okhttp-testing-support/src/main/java/okhttp3/TestUtil.java#L44
                .dispatcher(dispatcher)
                .addInterceptor(httpLoggingInterceptor)
                .eventListener(new CustomEventListener())
                .connectTimeout(Duration.ofSeconds(okHttpClientProperties.getConnectTimeout()))// default 10s
                .readTimeout(Duration.ofSeconds(okHttpClientProperties.getReadTimeout()))// default 10s
                .writeTimeout(Duration.ofSeconds(okHttpClientProperties.getWriteTimeout()))// default 10s
                .sslSocketFactory(IgnoreTsl.SOCKET_FACTORY, IgnoreTsl.TRUST_ALL_MANAGER)
                .hostnameVerifier((hostname, session) -> true)
                .retryOnConnectionFailure(false)
                .build();
    }

    public okhttp3.OkHttpClient getOkHttpClient() {
        return OkHttpClient;
    }
}
