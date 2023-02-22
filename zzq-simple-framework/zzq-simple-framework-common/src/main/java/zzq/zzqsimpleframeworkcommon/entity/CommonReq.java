package zzq.zzqsimpleframeworkcommon.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * http通用请求体
 *
 * @author zhouzhiqiang
 * @version 1.0
 * @date 2023-01-03 11:17
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class CommonReq<T> {

    /**
     * 数据对象
     */
    private T data;

}
