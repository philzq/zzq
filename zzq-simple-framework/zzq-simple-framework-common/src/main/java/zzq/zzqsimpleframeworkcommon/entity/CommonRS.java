package zzq.zzqsimpleframeworkcommon.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * http通用响应体
 *
 * @author zhouzhiqiang
 * @version 1.0
 * @date 2023-01-03 11:17
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class CommonRS<T> {

    /**
     * 通用code
     */
    private int code;

    /**
     * 业务code
     */
    private int businessCode;

    /**
     * 消息
     */
    private String message;

    /**
     * 数据对象
     */
    private T data;

    /**
     * 返回成功消息
     *
     * @return 成功消息
     */
    public static CommonRS<Object> success() {
        return CommonRS.success(HttpStatus.SUCCESS.getMessage());
    }

    /**
     * 返回成功数据
     *
     * @return 成功消息
     */
    public static <R> CommonRS<R> success(R data) {
        return CommonRS.success(HttpStatus.SUCCESS.getMessage(), data);
    }

    /**
     * 返回成功消息
     *
     * @param msg 返回内容
     * @return 成功消息
     */
    public static CommonRS<Object> success(String msg) {
        return CommonRS.success(msg, null);
    }

    /**
     * 返回成功消息
     *
     * @param msg  返回内容
     * @param data 数据对象
     * @return 成功消息
     */
    public static <R> CommonRS<R> success(String msg, R data) {
        CommonRS<R> rCommonRS = new CommonRS<>();
        rCommonRS.setCode(HttpStatus.SUCCESS.getCode());
        rCommonRS.setMessage(msg);
        rCommonRS.setData(data);
        return rCommonRS;
    }

    /**
     * 返回错误消息
     *
     * @return
     */
    public static CommonRS<Object> error() {
        return CommonRS.error(HttpStatus.ERROR.getMessage());
    }

    /**
     * 返回错误消息
     *
     * @param msg 返回内容
     * @return 警告消息
     */
    public static CommonRS<Object> error(String msg) {
        return CommonRS.error(msg, null);
    }

    /**
     * 返回错误消息
     *
     * @param msg  返回内容
     * @param data 数据对象
     * @return 警告消息
     */
    public static <R> CommonRS<R> error(String msg, R data) {
        CommonRS<R> rCommonRS = new CommonRS<>();
        rCommonRS.setCode(HttpStatus.ERROR.getCode());
        rCommonRS.setMessage(msg);
        rCommonRS.setData(data);
        return rCommonRS;
    }

    /**
     * 返回错误消息
     *
     * @param code 状态码
     * @param msg  返回内容
     * @return 警告消息
     */
    public static <R> CommonRS<R> error(int code, String msg) {
        CommonRS<R> rCommonRS = new CommonRS<>();
        rCommonRS.setCode(code);
        rCommonRS.setMessage(msg);
        return rCommonRS;
    }
}
