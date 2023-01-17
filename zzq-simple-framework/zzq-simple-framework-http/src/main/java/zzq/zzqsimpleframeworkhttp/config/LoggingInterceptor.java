package zzq.zzqsimpleframeworkhttp.config;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * 日志拦截器
 *
 * @author zhouzhiqiang
 * @version 1.0
 * @date 2023-01-17 14:45
 */
class LoggingInterceptor implements Interceptor {

    private LogLevel logLevel = LogLevel.BODY;

    public void setLogLevel(LogLevel logLevel) {
        this.logLevel = logLevel;
    }

    public enum LogLevel {
        NONE,
        BASIC,
        HEADERS,
        BODY
    }

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        long t1 = System.currentTimeMillis();
        Request request = chain.request();
        StringBuffer logStringBuffer = request.tag(StringBuffer.class);
        if (logStringBuffer != null) {
            logStringBuffer.append(String.format("Sending request %s %n%s",
                    request.url(), request.headers()));
        }
        Response response = chain.proceed(request);

        long t2 = System.currentTimeMillis();
        if (logStringBuffer != null) {
            logStringBuffer.append(String.format("Received response for %s in %sms%n%s",
                    request.url(), t2 - t1, response.headers()));
        }
        return response;
    }
}
