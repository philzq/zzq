package zzq.zzqsimpleframeworklog.util;

import ch.qos.logback.classic.spi.ILoggingEvent;
import zzq.zzqsimpleframeworkcommon.context.ProjectContext;
import zzq.zzqsimpleframeworkcommon.context.ThreadLocalManager;
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
        //如果projectContextThreadLocal有值直接取projectContextThreadLocal里面的值
        ProjectContext projectContext = ThreadLocalManager.projectContextThreadLocal.get();
        if (projectContext.getClassName() != null) {
            t.setClassName(projectContext.getClassName());
            t.setMethodName(projectContext.getMethodName());
        } else {
            Thread thread = Thread.currentThread();
            StackTraceElement[] stackTrace = thread.getStackTrace();
            for (int i = 1; i < stackTrace.length; i++) {
                StackTraceElement stackTraceElement = stackTrace[i];
                String className = stackTraceElement.getClassName();
                if (!className.contains(SystemConstant.PROJECT_PACKAGE)) {
                    t.setClassName(className);
                    t.setMethodName(stackTraceElement.getMethodName());
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
            baseLogEntity.setMethodName(callerData.getMethodName());
            baseLogEntity.setClassName(callerData.getClassName());
        }
    }
}
