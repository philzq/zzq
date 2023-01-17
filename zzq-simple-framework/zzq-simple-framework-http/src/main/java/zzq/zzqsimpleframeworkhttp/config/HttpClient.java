package zzq.zzqsimpleframeworkhttp.config;

import com.fasterxml.jackson.core.type.TypeReference;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zzq.zzqsimpleframeworkhttp.exception.HttpClientException;
import zzq.zzqsimpleframeworkjson.JacksonUtil;

import java.time.Duration;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;


public class HttpClient {

    private final Logger logger = LoggerFactory.getLogger(HttpClient.class);

    private final OkHttpClient okHttpClient;

    /**
     * 请求url前缀，简化调用及防止HttpClient窜用
     */
    private final String hostName;

    /**
     * @param maxIdleConnections 最大连接数
     */
    public HttpClient(String hostName, int maxIdleConnections) {
        this(hostName, maxIdleConnections, 15);
    }

    /**
     * @param maxIdleConnections 最大连接数
     * @param keepAlive          连接保持时长，单位秒
     */
    public HttpClient(String hostName, int maxIdleConnections, long keepAlive) {
        this(hostName, maxIdleConnections, keepAlive, 10, 10, 10);
    }

    /**
     * @param maxIdleConnections 最大连接数
     * @param keepAlive          连接保持时长，单位秒
     * @param connectTimeout     链接超时时间
     * @param readTimeout        读超时时间
     * @param writeTimeout       写超时时间
     */
    public HttpClient(String hostName, int maxIdleConnections, long keepAlive, long connectTimeout, long readTimeout, long writeTimeout) {
        this(hostName, maxIdleConnections, keepAlive, connectTimeout, readTimeout, writeTimeout, 64, 5);
    }

    /**
     * @param maxIdleConnections 最大连接数
     * @param keepAlive          连接保持时长，单位秒
     * @param connectTimeout     链接超时时间
     * @param readTimeout        读超时时间
     * @param writeTimeout       写超时时间
     * @param maxRequests        最大线程数，异步调用场景才用的到（同步调用可不关注）
     * @param maxRequestsPerHost 每台主机的最大并发数,只对异步请求生效 （同步调用可不关注）
     */
    public HttpClient(String hostName, int maxIdleConnections, long keepAlive, long connectTimeout, long readTimeout, long writeTimeout, int maxRequests, int maxRequestsPerHost) {
        if (hostName == null) {
            throw new HttpClientException("hostName 不能为空！");
        }
        this.hostName = hostName;
        Dispatcher dispatcher = new Dispatcher();
        dispatcher.setMaxRequests(maxRequests);
        dispatcher.setMaxRequestsPerHost(maxRequestsPerHost);

        ConnectionPool connectionPool = new ConnectionPool(maxIdleConnections, keepAlive, TimeUnit.SECONDS);

        okHttpClient = new OkHttpClient().newBuilder()
                .connectionPool(connectionPool) // 使用默认配置 https://github.com/square/okhttp/blob/master/okhttp-testing-support/src/main/java/okhttp3/TestUtil.java#L44
                .dispatcher(dispatcher)
                .addInterceptor(new LoggingInterceptor())
                .eventListener(new LoggingEventListener())
                .connectTimeout(Duration.ofSeconds(connectTimeout))// default 10s
                .readTimeout(Duration.ofSeconds(readTimeout))// default 10s
                .writeTimeout(Duration.ofSeconds(writeTimeout))// default 10s
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

    private String getToResponse(String relativePath, Map<String, String> param, Map<String, String> header) {
        String rs = null;
        Request request = null;
        try {
            String url = hostName + relativePath;
            Request.Builder requestBuilder = new Request.Builder().headers(toHeader(header)).tag(StringBuffer.class, new StringBuffer());
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
        } finally {
            if (request != null) {
                StringBuffer logTag = request.tag(StringBuffer.class);
                if (logTag != null) {
                    logger.info(logTag.toString());
                }
            }
        }
        return rs;
    }

    private String postToResponse(String relativePath, Object param, Map<String, String> header) {
        String rs = null;
        Request request = null;
        try {
            String url = hostName + relativePath;
            request = new Request.Builder().url(url).headers(toHeader(header)).tag(StringBuffer.class, new StringBuffer())
                    .post(RequestBody.create(param instanceof String ? (String) param :
                            Objects.requireNonNull(JacksonUtil.toJSon(param)), jsonMediaType)).build();
            Call r = okHttpClient.newCall(request);
            Response response = r.execute();
            rs = response.body() != null ? response.body().string() : null;
        } catch (Exception e) {
            logger.error("【HTTP调用异常】", e);
        } finally {
            if (request != null) {
                StringBuffer logTag = request.tag(StringBuffer.class);
                if (logTag != null) {
                    logger.info(logTag.toString());
                }
            }
        }
        return rs;
    }

    private String postToResponseByForm(String relativePath, Map<String, String> param, Map<String, String> header) {
        String rs = null;
        Request request = null;
        try {
            String url = hostName + relativePath;
            FormBody.Builder build = new FormBody.Builder();
            param.forEach(build::add);
            request = new Request.Builder().url(url).headers(toHeader(header)).tag(StringBuffer.class, new StringBuffer())
                    .post(build.build()).build();
            Call r = okHttpClient.newCall(request);
            Response response = r.execute();
            rs = response.body() != null ? response.body().string() : null;
        } catch (Exception e) {
            logger.error("【HTTP调用异常】", e);
        } finally {
            if (request != null) {
                StringBuffer logTag = request.tag(StringBuffer.class);
                if (logTag != null) {
                    logger.info(logTag.toString());
                }
            }
        }
        return rs;
    }

    private <T> T getToResponse(String relativePath, Map<String, String> param, Map<String, String> header, TypeReference<T> typeReference) {
        T result = null;
        String toResponse = getToResponse(relativePath, param, header);
        if (toResponse != null) {
            result = JacksonUtil.parseJson(toResponse, typeReference);
        }
        return result;
    }

    private <T> T postToResponse(String relativePath, Object param, Map<String, String> header, TypeReference<T> typeReference) {
        T result = null;
        String postToResponse = postToResponse(relativePath, param, header);
        if (postToResponse != null) {
            result = JacksonUtil.parseJson(postToResponse, typeReference);
        }
        return result;
    }

    private <T> T postToResponseByForm(String relativePath, Map<String, String> param, Map<String, String> header, TypeReference<T> typeReference) {
        T result = null;
        String postToResponseByForm = postToResponseByForm(relativePath, param, header);
        if (postToResponseByForm != null) {
            result = JacksonUtil.parseJson(postToResponseByForm, typeReference);
        }
        return result;
    }

    public <T> T get(String relativePath, TypeReference<T> typeReference) {
        T result = getToResponse(relativePath, null, null, typeReference);
        return result;
    }

    public <T> T get(String relativePath, Map<String, String> param, TypeReference<T> typeReference) {
        T toResponse = getToResponse(relativePath, param, null, typeReference);
        return toResponse;
    }

    public <T> T get(String relativePath, Map<String, String> param, Map<String, String> header, TypeReference<T> typeReference) {
        T toResponse = getToResponse(relativePath, param, header, typeReference);
        return toResponse;
    }

    public <T> T post(String relativePath, TypeReference<T> typeReference) {
        T postToResponse = postToResponse(relativePath, null, null, typeReference);
        return postToResponse;
    }

    public <T> T post(String relativePath, Object param, TypeReference<T> typeReference) {
        T t = postToResponse(relativePath, param, null, typeReference);
        return t;
    }

    public <T> T post(String relativePath, Object param, Map<String, String> header, TypeReference<T> typeReference) {
        T t = postToResponse(relativePath, param, header, typeReference);
        return t;
    }

    public <T> T postByForm(String relativePath, Map<String, String> param, TypeReference<T> typeReference) {
        T t = postToResponseByForm(relativePath, param, null, typeReference);
        return t;
    }

    public <T> T postByForm(String relativePath, Map<String, String> param, Map<String, String> header, TypeReference<T> typeReference) {
        T t = postToResponseByForm(relativePath, param, header, typeReference);
        return t;
    }

    public String get(String relativePath) {
        return getToResponse(relativePath, null, null);
    }

    public String get(String relativePath, Map<String, String> param) {
        return getToResponse(relativePath, param, null);
    }

    public String get(String relativePath, Map<String, String> param, Map<String, String> header) {
        return getToResponse(relativePath, param, header);
    }

    public String post(String relativePath) {
        return postToResponse(relativePath, null, null);
    }

    public String post(String relativePath, Object param) {
        return postToResponse(relativePath, param, null);
    }

    public String post(String relativePath, Object param, Map<String, String> header) {
        return postToResponse(relativePath, param, header);
    }


    public String postByForm(String relativePath, Map<String, String> param) {
        return postToResponseByForm(relativePath, param, null);
    }

    public String postByForm(String relativePath, Map<String, String> param, Map<String, String> header) {
        return postToResponseByForm(relativePath, param, header);
    }
}
