package zzq.zzqsimpleframeworkcommon.context;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 全局上下文 -- 存储服务与服务之前全链路上下文信息
 *
 * @author zhouzhiqiang
 * @version 1.0
 * @date 2023-02-15 10:34
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class GlobalContext {

    /**
     * 请求id  --  全链路跟踪
     */
    private String requestId;
}
