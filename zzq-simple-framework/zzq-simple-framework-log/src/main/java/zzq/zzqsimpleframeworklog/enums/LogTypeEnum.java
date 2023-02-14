package zzq.zzqsimpleframeworklog.enums;

import lombok.Getter;

/**
 * 日志类型枚举
 *
 * @author zhouzhiqiang
 * @version 1.0
 * @date 2023_02_09 15:05
 */
public enum LogTypeEnum {
    SYSTEM_INFO("system.info"),
    SYSTEM_ERROR("system.error"),
    ORDER_MONITOR("order.monitor"),
    REMOTE_DIGEST("remote.digest"),
    RPC_DIGEST("rpc.digest"),
    MONGO("mongo"),
    WEB_DIGEST("web.digest"),
    KAFKA_CONSUMER("kafka.consumer"),
    ACCOUNT_MONITOR("account.monitor"),
    API_MONITOR("api.monitor"),
    PULLING_MONITOR("pulling.monitor"),
    PUSH_MONITOR("push.monitor"),
    PUSHTIMELY_MONITOR("pushtimely.monitor"),
    RECEPTION_MONITOR("reception.monitor"),
    ROUTING_MONITOR("routing.monitor"),
    TIMELY_MONITOR("timely.monitor"),
    ;

    /**
     * 日志类型名称
     */
    @Getter
    private final String logTypeName;

    LogTypeEnum(String logTypeName) {
        this.logTypeName = logTypeName;
    }
}
