package zzq.zzqsimpleframeworkhttp.config;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import zzq.zzqsimpleframeworkcommon.context.ThreadLocalManager;
import zzq.zzqsimpleframeworklog.entity.RemoteDigestLogEntity;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

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
        LocalDateTime startTime = LocalDateTime.now();
        Request request = chain.request();
        RemoteDigestLogEntity remoteDigestLogEntity = request.tag(RemoteDigestLogEntity.class);
        if (remoteDigestLogEntity != null) {
            String requestId = ThreadLocalManager.globalContextThreadLocal.get().getRequestId();
            remoteDigestLogEntity.setRequestId(requestId);
            remoteDigestLogEntity.setUri(request.url().toString());
        }
        Response response = chain.proceed(request);

        if (remoteDigestLogEntity != null) {
            long elapseTime = Duration.between(startTime, LocalDateTime.now()).toMillis();
            remoteDigestLogEntity.setElapseTime(elapseTime);
            //最大只取1M以内的数据，防止响应体太大影响日志组件
            String body = response.peekBody(1024 * 1024).string();
            remoteDigestLogEntity.setResponse(body);
        }
        return response;
    }
}
