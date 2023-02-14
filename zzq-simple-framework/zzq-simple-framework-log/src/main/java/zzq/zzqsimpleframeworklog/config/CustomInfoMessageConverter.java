package zzq.zzqsimpleframeworklog.config;

import ch.qos.logback.classic.pattern.MessageConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import zzq.zzqsimpleframeworkjson.JacksonUtil;
import zzq.zzqsimpleframeworklog.entity.SystemInfoLogEntity;
import zzq.zzqsimpleframeworklog.util.LogInitUtil;

/**
 * 收集各种依赖组件输出的info日志
 * @author zhouzhiqiang
 * @version 1.0
 * @date 2023-02-13 13:55
 */
public class CustomInfoMessageConverter extends MessageConverter {

    public String convert(ILoggingEvent event) {
        SystemInfoLogEntity systemInfoLogEntity = SystemInfoLogEntity.builder()
                .title("依赖组件info日志")
                .message(event.getMessage())
                .className(event.getLoggerName())
                .build();

        //通过CallerData初始化必要字段
        LogInitUtil.initCommonByCallerData(event, systemInfoLogEntity);
        return JacksonUtil.toJSon(systemInfoLogEntity);
    }


}
