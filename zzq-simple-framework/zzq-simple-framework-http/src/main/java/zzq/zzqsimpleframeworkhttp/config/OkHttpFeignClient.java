package zzq.zzqsimpleframeworkhttp.config;

import feign.Client;
import okhttp3.*;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.*;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.util.Assert;
import zzq.zzqsimpleframeworkhttp.utils.HttpUtil;
import zzq.zzqsimpleframeworklog.entity.RemoteDigestLogEntity;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static feign.Util.enumForName;


public class OkHttpFeignClient implements Client {

    private static final Log LOG = LogFactory.getLog(OkHttpFeignClient.class);

    private LoadBalancerClient loadBalancerClient;

    private LoadBalancerClientFactory loadBalancerClientFactory;

    public void setLoadBalancerClient(LoadBalancerClient loadBalancerClient) {
        this.loadBalancerClient = loadBalancerClient;
    }

    public void setLoadBalancerClientFactory(LoadBalancerClientFactory loadBalancerClientFactory) {
        this.loadBalancerClientFactory = loadBalancerClientFactory;
    }

    private final OkHttpClient okHttpClient;

    /**
     * @param maxIdleConnections 最大连接数
     */
    public OkHttpFeignClient(int maxIdleConnections) {
        this(maxIdleConnections, 15);
    }

    /**
     * @param maxIdleConnections 最大连接数
     * @param keepAlive          连接保持时长，单位秒
     */
    public OkHttpFeignClient(int maxIdleConnections, long keepAlive) {
        this(maxIdleConnections, keepAlive, 10, 10, 10);
    }

    /**
     * @param maxIdleConnections 最大连接数
     * @param keepAlive          连接保持时长，单位秒
     * @param connectTimeout     链接超时时间
     * @param readTimeout        读超时时间
     * @param writeTimeout       写超时时间
     */
    public OkHttpFeignClient(int maxIdleConnections, long keepAlive, long connectTimeout, long readTimeout, long writeTimeout) {
        this(maxIdleConnections, keepAlive, connectTimeout, readTimeout, writeTimeout, 64, 5);
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
    public OkHttpFeignClient(int maxIdleConnections, long keepAlive, long connectTimeout, long readTimeout, long writeTimeout, int maxRequests, int maxRequestsPerHost) {
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

    public OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }

    private final MediaType jsonMediaType = MediaType.parse("application/json;charset=UTF-8");

    private Headers toHeader(Map<String, String> header) {
        if (header == null) {
            return Headers.of();
        }
        return Headers.of(header);
    }

    /***************************************集成FeignClient****************************************/

    @Override
    public feign.Response execute(feign.Request request, feign.Request.Options options) throws IOException {
        //如果有注册中心，需要对url进行处理@FeignClient(name = "user-api")
        if (loadBalancerClient != null && loadBalancerClientFactory != null) {
            //有注册中心走的逻辑
            final URI originalUri = URI.create(request.url());
            String serviceId = originalUri.getHost();
            Assert.state(serviceId != null, "Request URI does not contain a valid hostname: " + originalUri);
            String hint = getHint(serviceId);
            DefaultRequest<RequestDataContext> lbRequest = new DefaultRequest<>(
                    new RequestDataContext(buildRequestData(request), hint));
            Set<LoadBalancerLifecycle> supportedLifecycleProcessors = LoadBalancerLifecycleValidator
                    .getSupportedLifecycleProcessors(
                            loadBalancerClientFactory.getInstances(serviceId, LoadBalancerLifecycle.class),
                            RequestDataContext.class, ResponseData.class, ServiceInstance.class);
            supportedLifecycleProcessors.forEach(lifecycle -> lifecycle.onStart(lbRequest));
            ServiceInstance instance = loadBalancerClient.choose(serviceId, lbRequest);
            org.springframework.cloud.client.loadbalancer.Response<ServiceInstance> lbResponse = new DefaultResponse(
                    instance);
            if (instance == null) {
                String message = "Load balancer does not contain an instance for the service " + serviceId;
                if (LOG.isWarnEnabled()) {
                    LOG.warn(message);
                }
                supportedLifecycleProcessors.forEach(lifecycle -> lifecycle
                        .onComplete(new CompletionContext<ResponseData, ServiceInstance, RequestDataContext>(
                                CompletionContext.Status.DISCARD, lbRequest, lbResponse)));
                return feign.Response.builder().request(request).status(HttpStatus.SERVICE_UNAVAILABLE.value())
                        .body(message, StandardCharsets.UTF_8).build();
            }
            String reconstructedUrl = loadBalancerClient.reconstructURI(instance, originalUri).toString();

            request = buildRequest(request, reconstructedUrl);
        }
        Request okRequest = toOkHttpRequest(request);
        Response response = HttpUtil.sendWithResponse(okRequest, okHttpClient);
        return toFeignResponse(response, request).toBuilder().request(request).build();
    }

    static RequestData buildRequestData(feign.Request request) {
        HttpHeaders requestHeaders = new HttpHeaders();
        request.headers().forEach((key, value) -> requestHeaders.put(key, new ArrayList<>(value)));
        return new RequestData(HttpMethod.resolve(request.httpMethod().name()), URI.create(request.url()),
                requestHeaders, null, new HashMap<>());
    }

    protected feign.Request buildRequest(feign.Request request, String reconstructedUrl) {
        return feign.Request.create(request.httpMethod(), reconstructedUrl, request.headers(), request.body(),
                request.charset(), request.requestTemplate());
    }

    private String getHint(String serviceId) {
        LoadBalancerProperties properties = loadBalancerClientFactory.getProperties(serviceId);
        String defaultHint = properties.getHint().getOrDefault("default", "default");
        String hintPropertyValue = properties.getHint().get(serviceId);
        return hintPropertyValue != null ? hintPropertyValue : defaultHint;
    }

    private OkHttpClient getClient(feign.Request.Options options) {
        OkHttpClient requestScoped;
        if (okHttpClient.connectTimeoutMillis() != options.connectTimeoutMillis()
                || okHttpClient.readTimeoutMillis() != options.readTimeoutMillis()
                || okHttpClient.followRedirects() != options.isFollowRedirects()) {
            requestScoped = okHttpClient.newBuilder()
                    .connectTimeout(options.connectTimeoutMillis(), TimeUnit.MILLISECONDS)
                    .readTimeout(options.readTimeoutMillis(), TimeUnit.MILLISECONDS)
                    .followRedirects(options.isFollowRedirects())
                    .build();
        } else {
            requestScoped = okHttpClient;
        }
        return requestScoped;
    }

    private Request toOkHttpRequest(feign.Request input) {
        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.url(input.url());
        requestBuilder.tag(RemoteDigestLogEntity.class, new RemoteDigestLogEntity());

        MediaType mediaType = null;
        boolean hasAcceptHeader = false;
        for (String field : input.headers().keySet()) {
            if (field.equalsIgnoreCase("Accept")) {
                hasAcceptHeader = true;
            }

            for (String value : input.headers().get(field)) {
                requestBuilder.addHeader(field, value);
                if (field.equalsIgnoreCase("Content-Type")) {
                    mediaType = MediaType.parse(value);
                    if (input.charset() != null) {
                        mediaType.charset(input.charset());
                    }
                }
            }
        }
        // Some servers choke on the default accept string.
        if (!hasAcceptHeader) {
            requestBuilder.addHeader("Accept", "*/*");
        }

        byte[] inputBody = input.body();
        if (inputBody == null && !feign.Request.HttpMethod.GET.equals(input.httpMethod())) {
            // write an empty BODY to conform with okhttp 2.4.0+
            // http://johnfeng.github.io/blog/2015/06/30/okhttp-updates-post-wouldnt-be-allowed-to-have-null-body/
            inputBody = new byte[0];
        }

        RequestBody body = null;
        if (inputBody != null && !feign.Request.HttpMethod.GET.equals(input.httpMethod())) {
            body = RequestBody.create(inputBody, mediaType);
        }
        requestBuilder.method(input.httpMethod().name(), body);
        return requestBuilder.build();
    }

    private feign.Response toFeignResponse(Response response, feign.Request request)
            throws IOException {
        return feign.Response.builder()
                .protocolVersion(enumForName(feign.Request.ProtocolVersion.class, response.protocol()))
                .status(response.code())
                .reason(response.message())
                .request(request)
                .headers(toMap(response.headers()))
                .body(toBody(response.body()))
                .build();
    }

    private Map<String, Collection<String>> toMap(Headers headers) {
        return (Map) headers.toMultimap();
    }

    private feign.Response.Body toBody(final ResponseBody input) throws IOException {
        if (input == null || input.contentLength() == 0) {
            if (input != null) {
                input.close();
            }
            return null;
        }
        final Integer length = input.contentLength() >= 0 && input.contentLength() <= Integer.MAX_VALUE
                ? (int) input.contentLength()
                : null;

        return new feign.Response.Body() {

            @Override
            public void close() throws IOException {
                input.close();
            }

            @Override
            public Integer length() {
                return length;
            }

            @Override
            public boolean isRepeatable() {
                return false;
            }

            @Override
            public InputStream asInputStream() throws IOException {
                return input.byteStream();
            }

            @SuppressWarnings("deprecation")
            @Override
            public Reader asReader() throws IOException {
                return input.charStream();
            }

            @Override
            public Reader asReader(Charset charset) throws IOException {
                return asReader();
            }
        };
    }

}
