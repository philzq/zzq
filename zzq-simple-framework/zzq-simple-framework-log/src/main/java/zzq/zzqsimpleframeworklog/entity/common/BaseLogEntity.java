package zzq.zzqsimpleframeworklog.entity.common;

import zzq.zzqsimpleframeworklog.config.SystemConstant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 必要的字段
 *
 * @author zhouzhiqiang
 * @version 1.0
 * @date 2023-02-09 10:25
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class BaseLogEntity implements Serializable {

    /**
     * 应用名称
     */
    @Builder.Default
    private String appName = SystemConstant.APP_NAME;

    /**
     * 日志创建时间
     */
    @Builder.Default
    private LocalDateTime createTime = LocalDateTime.now();

    /**
     * 线程名
     */
    @Builder.Default
    private String threadName = Thread.currentThread().getName();

    /**
     * 主机ip
     */
    @Builder.Default
    private String hostIp = SystemConstant.HOST_IP;

    /**
     * 类名
     */
    private String className;

    /**
     * 方法名
     */
    private String methodName;

    /**
     * 行数
     */
    private int line;

    /**
     * 文件名
     */
    private String fileName;

}
