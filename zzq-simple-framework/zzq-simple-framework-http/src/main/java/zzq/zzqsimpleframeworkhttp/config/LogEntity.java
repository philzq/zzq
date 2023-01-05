package zzq.zzqsimpleframeworkhttp.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * 日志实体
 *
 * @author zhouzhiqiang
 * @version 1.0
 * @date 2023-01-05 9:50
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
class LogEntity {

    private final static Logger logger = LoggerFactory.getLogger(LogEntity.class);

    /**
     * 当前请求的id
     */
    @Builder.Default
    private String traceID = UUID.randomUUID().toString();

    /**
     * 记录当前请求事件中的开始时间 --- 用于输出各个时段的时间信息
     */
    private long startTime;

    /**
     * EventListener日志和HttpLoggingInterceptor log日志
     */
    @Builder.Default
    private StringBuilder log = new StringBuilder();

    /**
     * 收集日志
     *
     * @param content
     */
    public static void collectLog(String content) {
        collectLogWithTimeCycle(content, true);
    }

    /**
     * 不记录打印日志所处的时长，用于输出HttpLoggingInterceptor日志
     */
    public static void collectLogWithTimeCycle(String content, boolean outputTimeCycle) {
        LogEntity logEntity = HttpLogThreadLocal.logEntityTransmittableThreadLocal.get();
        if ("callStart".equals(content)) {//EventListener 最先执行，用于触发收集动作
            logEntity.setStartTime(System.currentTimeMillis());
            addLog("【traceID】" + logEntity.getTraceID(), false, logEntity);
        }
        addLog(content, outputTimeCycle, logEntity);
    }

    /**
     * 添加日志
     *
     * @param content
     * @param outputTimeCycle
     * @param logEntity
     */
    private static void addLog(String content, boolean outputTimeCycle, LogEntity logEntity) {
        logEntity.getLog()
                .append("\n")
                .append(LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm:ss.SSS")))
                .append("  ");
        if (outputTimeCycle) {
            //EventListener 才输出具体时间点
            long nowTime = System.currentTimeMillis();
            long elapsedTime = nowTime - logEntity.getStartTime();
            logEntity.getLog()
                    .append("【")
                    .append(elapsedTime)
                    .append("ms】 ");
        }
        logEntity.getLog().append(content);

        if("callFailed".equals(content)
                || "callEnd".equals(content)
        ){
            printfFinallyLog();
        }
    }

    /**
     * 记录日志并删除本地副本
     */
    public static void printfFinallyLog() {
        //打印日志
        //输出日志
        logger.info(HttpLogThreadLocal.logEntityTransmittableThreadLocal.get().getLog().toString());
        System.out.println(HttpLogThreadLocal.logEntityTransmittableThreadLocal.get().getLog().toString());
        HttpLogThreadLocal.remove();
    }
}
