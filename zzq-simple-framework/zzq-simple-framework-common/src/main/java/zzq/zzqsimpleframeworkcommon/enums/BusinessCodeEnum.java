package zzq.zzqsimpleframeworkcommon.enums;

import lombok.Getter;

/**
 * 通用业务code定义
 *
 * @author zhouzhiqiang
 * @version 1.0
 * @date 2023-02-07 13:53
 */
public enum BusinessCodeEnum {
    SYSTEM_EXCEPTION(500999, "系统异常"),
    FAIL_AUTHENTICATION(500901, "认证不通过"),
    NO_AUTHORITY(500902, "无权限"),
    REMOTE_DIGEST_EXCEPTION(500903,"Remote调用失败"),
    ;


    /**
     * 业务code
     */
    @Getter
    private int businessCode;

    /**
     * 消息
     */
    @Getter
    private String message;

    BusinessCodeEnum(int businessCode, String message) {
        this.businessCode = businessCode;
        this.message = message;
    }
}
