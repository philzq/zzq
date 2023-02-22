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
    /**
     * 业务info日志
     */
    SYSTEM_INFO("system.info"),
    /**
     * 业务error日志
     */
    SYSTEM_ERROR("system.error"),
    /**
     * 远程调用日志
     */
    REMOTE_DIGEST("remote.digest"),
    /**
     * rpc调用日志
     */
    RPC_DIGEST("rpc.digest"),
    /**
     * mongo 满语句日志
     */
    MONGO("mongo"),
    /**
     * web日志
     */
    WEB_DIGEST("web.digest"),
    /**
     * kafka生产日志
     */
    KAFKA("kafka"),
    /**
     * TASK日志
     */
    TASK("task"),
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
