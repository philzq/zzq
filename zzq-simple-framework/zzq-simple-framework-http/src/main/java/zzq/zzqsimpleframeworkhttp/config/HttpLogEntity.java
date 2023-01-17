package zzq.zzqsimpleframeworkhttp.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 日志实体
 *
 * @author zhouzhiqiang
 * @version 1.0
 * @date 2023-01-17 18:13
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HttpLogEntity {

    /**
     * 记录当前请求事件中的开始时间 --- 用于输出各个时段的时间信息
     */
    private long startTime;

    /**
     * EventListener日志和HttpLoggingInterceptor log日志
     */
    @Builder.Default
    private StringBuffer log = new StringBuffer();

    /**
     * 执行时长
     */
    private long elapsedTime;

}
