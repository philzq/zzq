package zzq.zzqsimpleframeworkhttp.config;

import com.fasterxml.jackson.core.type.TypeReference;
import okhttp3.*;
import okhttp3.logging.HttpLoggingInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zzq.zzqsimpleframeworkjson.JacksonUtil;

import java.time.Duration;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;


public class HttpClient {

    private final Logger logger = LoggerFactory.getLogger(HttpClient.class);

    private OkHttpClientProperties okHttpClientProperties;

    private OkHttpClient okHttpClient;

    public HttpClient(OkHttpClientProperties okHttpClientProperties) {
        Dispatcher dispatcher = new Dispatcher();
        dispatcher.setMaxRequests(okHttpClientProperties.getMaxRequests());
        dispatcher.setMaxRequestsPerHost(okHttpClientProperties.getMaxRequestsPerHost());

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

    private String getToResponse(String url, Map<String, String> param, Map<String, String> header) {
        String rs = null;
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
            rs = response.body() != null ? response.body().string() : null;
        } catch (Exception e) {
            logger.error("【HTTP调用异常】", e);
        }
        return rs;
    }

    private String postToResponse(String url, Object param, Map<String, String> header) {
        String rs = null;
        try {
            Request request = new Request.Builder().url(url).headers(toHeader(header))
                    .post(RequestBody.create(param instanceof String ? (String) param :
                            Objects.requireNonNull(JacksonUtil.toJSon(param)), jsonMediaType)).build();
            Call r = okHttpClient.newCall(request);
            Response response = r.execute();
            rs = response.body() != null ? response.body().string() : null;
        } catch (Exception e) {
            logger.error("【HTTP调用异常】", e);
        }
        return rs;
    }

    private String postToResponseByForm(String url, Map<String, String> param, Map<String, String> header) {
        String rs = null;
        try {
            FormBody.Builder build = new FormBody.Builder();
            param.forEach(build::add);
            Request request = new Request.Builder().url(url).headers(toHeader(header))
                    .post(build.build()).build();
            Call r = okHttpClient.newCall(request);
            Response response = r.execute();
            rs = response.body() != null ? response.body().string() : null;
        } catch (Exception e) {
            logger.error("【HTTP调用异常】", e);
        }
        return rs;
    }

    private <T> T getToResponse(String url, Map<String, String> param, Map<String, String> header, TypeReference<T> typeReference) {
        T result = null;
        String toResponse = getToResponse(url, param, header);
        if (toResponse != null) {
            result = JacksonUtil.parseJson(toResponse, typeReference);
        }
        return result;
    }

    private <T> T postToResponse(String url, Object param, Map<String, String> header, TypeReference<T> typeReference) {
        T result = null;
        String postToResponse = postToResponse(url, param, header);
        if (postToResponse != null) {
            result = JacksonUtil.parseJson(postToResponse, typeReference);
        }
        return result;
    }

    private <T> T postToResponseByForm(String url, Map<String, String> param, Map<String, String> header, TypeReference<T> typeReference) {
        T result = null;
        String postToResponseByForm = postToResponseByForm(url, param, header);
        if (postToResponseByForm != null) {
            result = JacksonUtil.parseJson(postToResponseByForm, typeReference);
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

    public String get(String url) {
        return getToResponse(url, null, null);
    }

    public String get(String url, Map<String, String> param) {
        return getToResponse(url, param, null);
    }

    public String get(String url, Map<String, String> param, Map<String, String> header) {
        return getToResponse(url, param, header);
    }

    public String post(String url) {
        return postToResponse(url, null, null);
    }

    public String post(String url, Object param) {
        return postToResponse(url, param, null);
    }

    public String post(String url, Object param, Map<String, String> header) {
        return postToResponse(url, param, header);
    }


    public String postByForm(String url, Map<String, String> param) {
        return postToResponseByForm(url, param, null);
    }

    public String postByForm(String url, Map<String, String> param, Map<String, String> header) {
        return postToResponseByForm(url, param, header);
    }
}
