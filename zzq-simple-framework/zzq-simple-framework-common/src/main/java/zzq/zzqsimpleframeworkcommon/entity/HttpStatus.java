package zzq.zzqsimpleframeworkcommon.entity;

import lombok.Getter;

/**
 * 返回状态码
 */
public enum HttpStatus {
    SUCCESS(200,"操作成功"),
    BAD_REQUEST(400,"参数列表错误"),
    ERROR(500,"系统内部错误")
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
