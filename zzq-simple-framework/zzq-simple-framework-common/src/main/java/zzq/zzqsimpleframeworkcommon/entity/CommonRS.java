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

    private HeadRS head;

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
        return CommonRS.success("操作成功");
    }

    /**
     * 返回成功数据
     *
     * @return 成功消息
     */
    public static <R> CommonRS<R> success(R data) {
        return CommonRS.success("操作成功", data);
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
        HeadRS headRS = new HeadRS();
        headRS.setCode(HttpStatus.SUCCESS);
        headRS.setMsg(msg);
        return new CommonRS<R>(headRS, data);
    }

    /**
     * 返回错误消息
     *
     * @return
     */
    public static CommonRS<Object> error() {
        return CommonRS.error("操作失败");
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
        HeadRS headRS = new HeadRS();
        headRS.setCode(HttpStatus.ERROR);
        headRS.setMsg(msg);
        return new CommonRS<R>(headRS, data);
    }

    /**
     * 返回错误消息
     *
     * @param code 状态码
     * @param msg  返回内容
     * @return 警告消息
     */
    public static CommonRS<Object> error(int code, String msg) {
        HeadRS headRS = new HeadRS();
        headRS.setCode(code);
        headRS.setMsg(msg);
        return new CommonRS<>(headRS, null);
    }

    @Data
    public static class HeadRS {

        /**
         * 状态码
         */
        private int code = HttpStatus.SUCCESS;

        /**
         * 返回内容
         */
        private String msg = "操作成功";

    }
}
