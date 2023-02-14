package zzq.zzqsimpleframeworklog.entity.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * 全局的基础日志
 *
 * @author zhouzhiqiang
 * @version 1.0
 * @date 2023-02-09 17:24
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class BaseGlobalLogEntity extends BaseLogEntity{

    /**
     * 开始时间
     */
    private LocalDateTime startTime;

    /**
     * 执行时间 ， 单位秒
     */
    private long elapseTime;

    /**
     * 请求id
     */
    private String requestId;

    /**
     * 是否成功
     */
    private boolean success;

    /**
     * 请求资源  --  http请求为URL,非http请求则记录响应执行的方法路径
     */
    private String uri;

    /**
     * 请求参数  --  http请求则记录请求参数，非http请求则记录方法入参
     */
    private String request;

    /**
     * 响应参数  --  http请求则记录响应参数，非http请求则记录方法出参
     */
    private String response;
}
