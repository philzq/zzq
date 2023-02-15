package zzq.zzqsimpleframeworklog.util;

import ch.qos.logback.classic.spi.ILoggingEvent;
import zzq.zzqsimpleframeworklog.config.SystemConstant;
import zzq.zzqsimpleframeworklog.entity.common.BaseLogEntity;

/**
 * @author zhouzhiqiang
 * @version 1.0
 * @date 2023-02-10 17:26
 */
public class LogInitUtil {

    /**
     * 获取打印日志的classname
     *
     * @return
     */
    private static void getLogClassNameAndInitClassAndMethod(BaseLogEntity t) {
        //如果已经传过了类名，则不填充类相关属性----如aop切面需要自己传值等
        if (t.getClassName() == null) {
            Thread thread = Thread.currentThread();
            StackTraceElement[] stackTrace = thread.getStackTrace();
            for (int i = 1; i < stackTrace.length; i++) {
                StackTraceElement stackTraceElement = stackTrace[i];
                String className = stackTraceElement.getClassName();
                if (!className.contains(SystemConstant.PROJECT_PACKAGE)) {
                    t.setClassName(className);
                    t.setMethodName(stackTraceElement.getMethodName());
                    t.setLine(stackTraceElement.getLineNumber());
                    t.setFileName(stackTraceElement.getFileName());
                    break;
                }
            }
        }
    }

    /**
     * 初始化通用字段
     *
     * @param t
     */
    public static void initCommonFields(BaseLogEntity t) {
        if (t != null) {
            getLogClassNameAndInitClassAndMethod(t);
        }
    }

    /**
     * 通过CallerData初始化必要字段
     *
     * @param event
     * @param baseLogEntity
     */
    public static void initCommonByCallerData(ILoggingEvent event, BaseLogEntity baseLogEntity) {
        if (event.hasCallerData()) {
            StackTraceElement callerData = event.getCallerData()[0];
            baseLogEntity.setLine(callerData.getLineNumber());
            baseLogEntity.setMethodName(callerData.getMethodName());
            baseLogEntity.setClassName(callerData.getClassName());
            baseLogEntity.setFileName(callerData.getFileName());
        }
    }
}
