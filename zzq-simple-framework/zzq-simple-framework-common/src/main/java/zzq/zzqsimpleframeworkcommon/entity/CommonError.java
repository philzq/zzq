package zzq.zzqsimpleframeworkcommon.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 异常实体
 *
 * @author zhouzhiqiang
 * @version 1.0
 * @date 2023-01-12 10:35
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class CommonError {

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
}
