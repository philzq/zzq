package zzq.zzqsimpleframeworkcommon.entity;

import zzq.zzqsimpleframeworkcommon.enums.BusinessCodeEnum;
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
public class CommonRsp<T> {

    /**
     * 通用code
     */
    private int code;

    /**
     * 业务code
     * 分为200、400、500三大类，后缀3位代表具体错误，组合在一起构成业务编码，业务编码由业务系统各自维护
     * ≤100的编码不使用，900号段预留给系统，如500999为未识别的系统异常
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
    public static CommonRsp<Object> success() {
        return CommonRsp.success(HttpStatus.SUCCESS.getMessage());
    }

    /**
     * 返回成功数据
     *
     * @return 成功消息
     */
    public static <R> CommonRsp<R> success(R data) {
        return CommonRsp.success(HttpStatus.SUCCESS.getMessage(), data);
    }

    /**
     * 返回成功消息
     *
     * @param msg 返回内容
     * @return 成功消息
     */
    public static CommonRsp<Object> success(String msg) {
        return CommonRsp.success(msg, null);
    }

    /**
     * 返回成功消息
     *
     * @param msg  返回内容
     * @param data 数据对象
     * @return 成功消息
     */
    public static <R> CommonRsp<R> success(String msg, R data) {
        CommonRsp<R> rCommonRsp = new CommonRsp<>();
        rCommonRsp.setCode(HttpStatus.SUCCESS.getCode());
        rCommonRsp.setMessage(msg);
        rCommonRsp.setData(data);
        return rCommonRsp;
    }

    /**
     * 返回错误消息
     *
     * @return
     */
    public static CommonRsp<Object> error() {
        return CommonRsp.error(HttpStatus.ERROR.getMessage());
    }

    /**
     * 返回错误消息
     *
     * @param msg 返回内容
     * @return 警告消息
     */
    public static CommonRsp<Object> error(String msg) {
        return CommonRsp.error(HttpStatus.ERROR.getCode(), BusinessCodeEnum.SYSTEM_EXCEPTION.getBusinessCode(), msg);
    }

    /**
     * 返回错误消息
     *
     * @param code 状态码
     * @param msg  返回内容
     * @return 警告消息
     */
    public static <R> CommonRsp<R> error(int code, int businessCode, String msg) {
        CommonRsp<R> rCommonRsp = new CommonRsp<>();
        rCommonRsp.setCode(code);
        rCommonRsp.setBusinessCode(businessCode);
        rCommonRsp.setMessage(msg);
        return rCommonRsp;
    }
}
