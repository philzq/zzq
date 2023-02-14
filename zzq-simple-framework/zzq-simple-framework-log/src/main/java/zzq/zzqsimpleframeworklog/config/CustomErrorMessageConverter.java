package zzq.zzqsimpleframeworklog.config;

import ch.qos.logback.classic.pattern.MessageConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.StackTraceElementProxy;
import ch.qos.logback.classic.spi.ThrowableProxyUtil;
import ch.qos.logback.core.CoreConstants;
import zzq.zzqsimpleframeworkjson.JacksonUtil;
import zzq.zzqsimpleframeworklog.entity.SystemErrorLogEntity;

/**
 * 收集各种依赖组件输出的err日志
 * @author zhouzhiqiang
 * @version 1.0
 * @date 2023-02-13 13:55
 */
public class CustomErrorMessageConverter extends MessageConverter {

    public String convert(ILoggingEvent event) {
        SystemErrorLogEntity systemErrorLogEntity = SystemErrorLogEntity.builder()
                .title("依赖组件error日志")
                .message(event.getMessage())
                .className(event.getLoggerName())
                .errorCode(500)   // 此处可根据业务自定义  依赖组件的异常code哦
                .errorMessage("依赖组件异常")
                .build();

        //通过StackTrace初始化必要字段
        initCommonByStackTrace(event, systemErrorLogEntity);
        return JacksonUtil.toJSon(systemErrorLogEntity);
    }

    /**
     * 获取堆栈信息
     *
     * @param evt
     * @return
     */
    private void initCommonByStackTrace(ILoggingEvent evt, SystemErrorLogEntity systemErrorLogEntity) {
        try {
            IThrowableProxy proxy = evt.getThrowableProxy();
            if (proxy == null)
                return;

            StringBuilder builder = new StringBuilder();
            for (StackTraceElementProxy step : proxy
                    .getStackTraceElementProxyArray()) {
                String string = step.toString();
                builder.append(CoreConstants.TAB).append(string);
                ThrowableProxyUtil.subjoinPackagingData(builder, step);
                builder.append(CoreConstants.LINE_SEPARATOR);
            }
            systemErrorLogEntity.setStackTrace(builder.toString());

            if (proxy.getStackTraceElementProxyArray() != null && proxy.getStackTraceElementProxyArray().length >= 1) {
                StackTraceElement stackTraceElement = proxy.getStackTraceElementProxyArray()[0].getStackTraceElement();
                systemErrorLogEntity.setLine(stackTraceElement.getLineNumber());
                systemErrorLogEntity.setMethodName(stackTraceElement.getMethodName());
                systemErrorLogEntity.setClassName(stackTraceElement.getClassName());
                systemErrorLogEntity.setFileName(stackTraceElement.getFileName());
            }
        } catch (Exception e) {
            addError("exception trying to log exception", e);
        }
    }
}
