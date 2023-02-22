package zzq.zzqsimpleframeworkcommon.entity;

import lombok.Getter;

/**
 * 返回状态码
 */
public enum HttpStatus {
    SUCCESS(200, "操作成功"),
    BAD_REQUEST(400, "请求参数异常，如参数缺失、格式不匹配、数据类型不匹配"),
    ERROR(500, "后台处理失败，如用户不存在、密码错误"),
    ;

    @Getter
    private int code;

    @Getter
    private String message;

    HttpStatus(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
