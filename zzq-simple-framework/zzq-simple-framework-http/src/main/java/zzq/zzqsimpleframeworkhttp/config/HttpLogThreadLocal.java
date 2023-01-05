package zzq.zzqsimpleframeworkhttp.config;

import com.alibaba.ttl.TransmittableThreadLocal;

/**
 * okhttp 日志输出各个阶段的信息
 * 由于okhttp的事件日志收集不能统一，所以使用本地副本来传递上线文信息输入日志
 *
 * @author zhouzhiqiang
 * @version 1.0
 * @date 2023-01-04 15:01
 */
class HttpLogThreadLocal {

    static final TransmittableThreadLocal<LogEntity> logEntityTransmittableThreadLocal = new TransmittableThreadLocal<>() {
        @Override
        protected LogEntity initialValue() {
            return LogEntity.builder().build();
        }
    };

    static void remove() {
        HttpLogThreadLocal.logEntityTransmittableThreadLocal.remove();
    }
}
