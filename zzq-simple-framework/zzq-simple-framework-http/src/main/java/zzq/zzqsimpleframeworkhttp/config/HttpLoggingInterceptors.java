package zzq.zzqsimpleframeworkhttp.config;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * 日志拦截器
 *
 * @author zhouzhiqiang
 * @version 1.0
 * @date 2023-01-05 9:37
 */
class HttpLoggingInterceptors {

    private final static Logger logger = LoggerFactory.getLogger(HttpLoggingInterceptors.class);

    /**
     * 该拦截器优先级最高，最先执行，用于统计输出HttpLoggingInterceptor 和 EventListener 日志
     */
    public static final CustomLoggingInterceptor customLoggingInterceptor = new CustomLoggingInterceptor();

    public static final HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(content -> LogEntity.collectLogWithTimeCycle(content, false));

    static {
        httpLoggingInterceptor.level(HttpLoggingInterceptor.Level.BODY);
    }

    /**
     * 统一输出日志的地方，输出日志后销毁HttpLogThreadLocal
     */
    public static class CustomLoggingInterceptor implements Interceptor {
        @NotNull
        @Override
        public Response intercept(@NotNull Chain chain) throws IOException {
            Response response = null;
            try {
                HttpLogThreadLocal.logEntityTransmittableThreadLocal.get().setRecordLog(true);
                Request request = chain.request();
                response = chain.proceed(request);
            } finally {
                LogEntity.collectLog("elapseTime");
                //输出日志
                logger.info(HttpLogThreadLocal.logEntityTransmittableThreadLocal.get().getLog().toString());
                System.out.println(HttpLogThreadLocal.logEntityTransmittableThreadLocal.get().getLog().toString());
                HttpLogThreadLocal.remove();
            }
            return response;
        }
    }
}
