package zzq.zzqsimpleframeworkhttp.config;

import com.fasterxml.jackson.core.type.TypeReference;
import okhttp3.*;
import okhttp3.logging.HttpLoggingInterceptor;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zzq.zzqsimpleframeworkjson.JacksonUtil;

import java.time.Duration;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;


public class HttpClient {

    private final static Logger logger = LoggerFactory.getLogger(HttpClient.class);

    private OkHttpClientProperties okHttpClientProperties;

    private OkHttpClient okHttpClient;

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

        ConnectionPool connectionPool = new ConnectionPool(okHttpClientProperties.getMaxIdleConnections(), 15, TimeUnit.SECONDS);

        okHttpClient = new OkHttpClient().newBuilder()
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
        return okHttpClient;
    }

    private final MediaType jsonMediaType = MediaType.parse("application/json;charset=UTF-8");

    private Headers toHeader(Map<String, String> header) {
        if (header == null) {
            return Headers.of();
        }
        return Headers.of(header);
    }

    private <T> T getToResponse(String url, Map<String, String> param, Map<String, String> header, TypeReference<T> typeReference) {
        T result = null;
        try {
            Request request;
            Request.Builder requestBuilder = new Request.Builder().headers(toHeader(header));
            if (param != null) {
                HttpUrl httpUrl = HttpUrl.parse(url);
                if (httpUrl == null) {
                    throw new RuntimeException("request url error: " + url);
                }
                HttpUrl.Builder urlBuilder = httpUrl.newBuilder();
                param.forEach(urlBuilder::addQueryParameter);
                request = requestBuilder.url(urlBuilder.build()).build();
            } else {
                request = requestBuilder.url(url).build();
            }
            Call r = okHttpClient.newCall(request);
            Response response = r.execute();
            if (ObjectUtils.isNotEmpty(response.body())) {
                String body = response.body().string();
                result = JacksonUtil.parseJson(body, typeReference);
            }
        } catch (Exception e) {
            logger.error("【HTTP调用异常】", e);
        }
        return result;
    }

    private <T> T postToResponse(String url, Object param, Map<String, String> header, TypeReference<T> typeReference) {
        T result = null;
        try {
            Request request = new Request.Builder().url(url).headers(toHeader(header))
                    .post(RequestBody.create(jsonMediaType, param instanceof String ? (String) param :
                            Objects.requireNonNull(JacksonUtil.toJSon(param)))).build();
            Call r = okHttpClient.newCall(request);
            Response response = r.execute();
            if (ObjectUtils.isNotEmpty(response.body())) {
                String body = response.body().string();
                result = JacksonUtil.parseJson(body, typeReference);
            }
        } catch (Exception e) {
            logger.error("【HTTP调用异常】", e);
        }
        return result;
    }

    private <T> T postToResponseByForm(String url, Map<String, String> param, Map<String, String> header, TypeReference<T> typeReference) {
        T result = null;
        try {
            FormBody.Builder build = new FormBody.Builder();
            param.forEach(build::add);
            Request request = new Request.Builder().url(url).headers(toHeader(header))
                    .post(build.build()).build();
            Call r = okHttpClient.newCall(request);
            Response response = r.execute();
            if (ObjectUtils.isNotEmpty(response.body())) {
                String body = response.body().string();
                result = JacksonUtil.parseJson(body, typeReference);
            }
        } catch (Exception e) {
            logger.error("【HTTP调用异常】", e);
        }
        return result;
    }

    public <T> T get(String url, TypeReference<T> typeReference) {
        T result = getToResponse(url, null, null, typeReference);
        return result;
    }

    public <T> T get(String url, Map<String, String> param, TypeReference<T> typeReference) {
        T toResponse = getToResponse(url, param, null, typeReference);
        return toResponse;
    }

    public <T> T get(String url, Map<String, String> param, Map<String, String> header, TypeReference<T> typeReference) {
        T toResponse = getToResponse(url, param, header, typeReference);
        return toResponse;
    }

    public <T> T post(String url, TypeReference<T> typeReference) {
        T postToResponse = postToResponse(url, null, null, typeReference);
        return postToResponse;
    }

    public <T> T post(String url, Object param, TypeReference<T> typeReference) {
        T t = postToResponse(url, param, null, typeReference);
        return t;
    }

    public <T> T post(String url, Object param, Map<String, String> header, TypeReference<T> typeReference) {
        T t = postToResponse(url, param, header, typeReference);
        return t;
    }

    public <T> T postByForm(String url, Map<String, String> param, TypeReference<T> typeReference) {
        T t = postToResponseByForm(url, param, null, typeReference);
        return t;
    }

    public <T> T postByForm(String url, Map<String, String> param, Map<String, String> header, TypeReference<T> typeReference) {
        T t = postToResponseByForm(url, param, header, typeReference);
        return t;
    }
}
