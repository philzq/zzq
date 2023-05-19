package zzq.zzqsimpleframeworkhttp.config;

import feign.Client;
import okhttp3.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import zzq.zzqsimpleframeworkhttp.utils.HttpUtil;
import zzq.zzqsimpleframeworklog.entity.RemoteDigestLogEntity;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.Charset;
import java.time.Duration;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static feign.Util.enumForName;


public class OkHttpFeignClient implements Client {

    private static final Log LOG = LogFactory.getLog(OkHttpFeignClient.class);

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
        Request okRequest = toOkHttpRequest(request);
        Response response = HttpUtil.sendWithResponse(okRequest, okHttpClient);
        return toFeignResponse(response, request).toBuilder().request(request).build();
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
