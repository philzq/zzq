package zzq.zzqsimpleframeworklog.entity;

import zzq.zzqsimpleframeworklog.entity.common.BaseLogEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author zhouzhiqiang
 * @version 1.0
 * @date 2023-02-09 10:33
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class SystemErrorLogEntity extends BaseLogEntity {

    /**
     * 标题
     */
    private String title;

    /**
     * 消息体
     */
    private String message;

    /**
     * 错误码
     */
    private int errorCode;

    /**
     * 错误描述
     */
    private String errorMessage;

    /**
     * 堆栈信息
     */
    private String stackTrace;


}
