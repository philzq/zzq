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
        HttpLogEntity httpLogEntity = request.tag(HttpLogEntity.class);
        if (httpLogEntity != null) {
            httpLogEntity.getLog().append(String.format("Sending request %s %n%s",
                    request.url(), request.headers()));
        }
        Response response = chain.proceed(request);

        long t2 = System.currentTimeMillis();
        if (httpLogEntity != null) {
            long elapsedTime = t2 - t1;
            httpLogEntity.setElapsedTime(elapsedTime);
            //最大只取1M以内的数据，防止响应体太大影响日志组件
            String body = response.peekBody(1024 * 1024).string();
            httpLogEntity.getLog().append(String.format("Received response for %s in %sms%n%s%s",
                    request.url(), elapsedTime, response.headers(), body));
        }
        return response;
    }
}
