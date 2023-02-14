package zzq.zzqsimpleframeworklog;

import zzq.zzqsimpleframeworkjson.JacksonUtil;
import zzq.zzqsimpleframeworklog.entity.SystemErrorLogEntity;
import zzq.zzqsimpleframeworklog.util.LogInitUtil;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;

/**
 * 日志工具类  -- 基础的使用，，只有SystemInfo和SystemError应用场景
 *
 * @author zhouzhiqiang
 * @version 1.0
 * @date 2023-02-09 10:19
 */
public class LogErrorBasicUtil {

    private final Logger logger;

    public LogErrorBasicUtil(Logger logger) {
        this.logger = logger;
    }

    /*******************************error*********************************/
    public void error(String title, String message) {
        error(title, message, null);
    }

    public void error(String title, String message, Throwable throwable) {
        error(title, message, 0, null, throwable);
    }

    public void error(String title, String message, int errorCode, String errorMessage) {
        error(title, message, errorCode, errorMessage, null);
    }

    public void error(String title, String message, int errorCode, String errorMessage, Throwable throwable) {
        SystemErrorLogEntity systemErrorLogEntity = SystemErrorLogEntity.builder()
                .title(title)
                .message(message)
                .errorCode(errorCode)
                .errorMessage(errorMessage)
                .build();
        if (throwable != null) {
            systemErrorLogEntity.setStackTrace(ExceptionUtils.getStackTrace(throwable));
        }
        error(systemErrorLogEntity);
    }

    public void error(SystemErrorLogEntity t) {
        //初始化通用字段
        LogInitUtil.initCommonFields(t);
        logger.error(JacksonUtil.toJSon(t));
    }
    /*******************************error*********************************/
}
