package zzq.zzqsimpleframeworklog;

import zzq.zzqsimpleframeworklog.entity.*;
import zzq.zzqsimpleframeworklog.entity.common.BaseLogEntity;
import zzq.zzqsimpleframeworklog.enums.LogTypeEnum;
import zzq.zzqsimpleframeworklog.exception.LogException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * LogUtil获取LogUtil
 *
 * @author zhouzhiqiang
 * @version 1.0
 * @date 2023-02-09 10:36
 */
public interface LogUtilFactory {

    LogInfoBasicUtil SYSTEM_INFO = LogUtilFactory.getSystemInfoLogUtil();
    LogErrorBasicUtil SYSTEM_ERROR = LogUtilFactory.getSystemErrorLogUtil();

    LogAdvancedUtil<WebDigestLogEntity> WEB_DIGEST = LogUtilFactory.getLogUtil(LogTypeEnum.WEB_DIGEST, WebDigestLogEntity.class);
    LogAdvancedUtil<KafkaLogEntity> KAFKA = LogUtilFactory.getLogUtil(LogTypeEnum.KAFKA, KafkaLogEntity.class);
    LogAdvancedUtil<MongoLogEntity> MONGO = LogUtilFactory.getLogUtil(LogTypeEnum.MONGO, MongoLogEntity.class);
    LogAdvancedUtil<RemoteDigestLogEntity> REMOTE_DIGEST = LogUtilFactory.getLogUtil(LogTypeEnum.REMOTE_DIGEST, RemoteDigestLogEntity.class);
    LogAdvancedUtil<RpcDigestLogEntity> RPC_DIGEST = LogUtilFactory.getLogUtil(LogTypeEnum.RPC_DIGEST, RpcDigestLogEntity.class);
    LogAdvancedUtil<TaskLogEntity> TASK = LogUtilFactory.getLogUtil(LogTypeEnum.TASK, TaskLogEntity.class);


    /**
     * 获取业务日志util，支持业务自定义日志场景
     *
     * @param logTypeName
     * @param paramClass
     * @param <T>
     * @return
     */
    static <T extends BaseLogEntity> LogAdvancedUtil<T> getBusinessLogUtil(String logTypeName, Class<T> paramClass) {
        if (logTypeName == null || logTypeName.trim().length() == 0 || paramClass == null) {
            throw new LogException("logTypeName和paramClass不能为null");
        }
        Logger logger = LoggerFactory.getLogger(logTypeName);
        LogAdvancedUtil<T> logBusinessLogUtil = new LogAdvancedUtil<>(logger, paramClass);
        return logBusinessLogUtil;
    }

    /**
     * 获取日志组件
     *
     * @param logTypeEnum 日志类型 -- 如果存在新的非定制化的日志类型，联系架构组加上
     * @param t
     * @return
     */
    private static <T extends BaseLogEntity> LogAdvancedUtil<T> getLogUtil(LogTypeEnum logTypeEnum, Class<T> t) {
        Logger logger = LoggerFactory.getLogger(logTypeEnum.getLogTypeName());
        LogAdvancedUtil<T> logAdvancedUtil = new LogAdvancedUtil<>(logger, t);
        return logAdvancedUtil;
    }

    /**
     * 获取日志组件 --- 只用于info
     *
     * @return
     */
    private static LogInfoBasicUtil getSystemInfoLogUtil() {
        Logger logger = LoggerFactory.getLogger(LogTypeEnum.SYSTEM_INFO.getLogTypeName());
        LogInfoBasicUtil logInfoBasicUtil = new LogInfoBasicUtil(logger);
        return logInfoBasicUtil;
    }

    /**
     * 获取日志组件 --- 只用于error
     *
     * @return
     */
    private static LogErrorBasicUtil getSystemErrorLogUtil() {
        Logger logger = LoggerFactory.getLogger(LogTypeEnum.SYSTEM_ERROR.getLogTypeName());
        LogErrorBasicUtil logErrorBasicUtil = new LogErrorBasicUtil(logger);
        return logErrorBasicUtil;
    }

}
