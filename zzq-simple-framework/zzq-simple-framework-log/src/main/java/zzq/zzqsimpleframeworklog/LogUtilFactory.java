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

    zzq.zzqsimpleframeworklog.LogInfoBasicUtil SYSTEM_INFO = zzq.zzqsimpleframeworklog.LogUtilFactory.getSystemInfoLogUtil();
    zzq.zzqsimpleframeworklog.LogErrorBasicUtil SYSTEM_ERROR = zzq.zzqsimpleframeworklog.LogUtilFactory.getSystemErrorLogUtil();

    zzq.zzqsimpleframeworklog.LogAdvancedUtil<WebDigestLogEntity> WEB_DIGEST = zzq.zzqsimpleframeworklog.LogUtilFactory.getLogUtil(LogTypeEnum.WEB_DIGEST, WebDigestLogEntity.class);
    zzq.zzqsimpleframeworklog.LogAdvancedUtil<KafkaConsumerLogEntity> KAFKA_CONSUMER = zzq.zzqsimpleframeworklog.LogUtilFactory.getLogUtil(LogTypeEnum.KAFKA_CONSUMER, KafkaConsumerLogEntity.class);
    zzq.zzqsimpleframeworklog.LogAdvancedUtil<MongoLogEntity> MONGO = zzq.zzqsimpleframeworklog.LogUtilFactory.getLogUtil(LogTypeEnum.MONGO, MongoLogEntity.class);
    zzq.zzqsimpleframeworklog.LogAdvancedUtil<RemoteDigestLogEntity> REMOTE_DIGEST = zzq.zzqsimpleframeworklog.LogUtilFactory.getLogUtil(LogTypeEnum.REMOTE_DIGEST, RemoteDigestLogEntity.class);
    zzq.zzqsimpleframeworklog.LogAdvancedUtil<RpcDigestLogEntity> RPC_DIGEST = zzq.zzqsimpleframeworklog.LogUtilFactory.getLogUtil(LogTypeEnum.RPC_DIGEST, RpcDigestLogEntity.class);
    zzq.zzqsimpleframeworklog.LogAdvancedUtil<TaskLogEntity> TASK = zzq.zzqsimpleframeworklog.LogUtilFactory.getLogUtil(LogTypeEnum.TASK, TaskLogEntity.class);


    /**
     * 获取业务日志util，支持业务自定义日志场景
     *
     * @param logTypeName
     * @param paramClass
     * @param <T>
     * @return
     */
    static <T extends BaseLogEntity> zzq.zzqsimpleframeworklog.LogAdvancedUtil<T> getBusinessLogUtil(String logTypeName, Class<T> paramClass) {
        if (logTypeName == null || logTypeName.trim().length() == 0 || paramClass == null) {
            throw new LogException("logTypeName和paramClass不能为null");
        }
        Logger logger = LoggerFactory.getLogger(logTypeName);
        zzq.zzqsimpleframeworklog.LogAdvancedUtil<T> logBusinessLogUtil = new zzq.zzqsimpleframeworklog.LogAdvancedUtil<>(logger, paramClass);
        return logBusinessLogUtil;
    }

    /**
     * 获取日志组件
     *
     * @param logTypeEnum 日志类型 -- 如果存在新的非定制化的日志类型，联系架构组加上
     * @param t
     * @return
     */
    private static <T extends BaseLogEntity> zzq.zzqsimpleframeworklog.LogAdvancedUtil<T> getLogUtil(LogTypeEnum logTypeEnum, Class<T> t) {
        Logger logger = LoggerFactory.getLogger(logTypeEnum.getLogTypeName());
        zzq.zzqsimpleframeworklog.LogAdvancedUtil<T> logAdvancedUtil = new zzq.zzqsimpleframeworklog.LogAdvancedUtil<>(logger, t);
        return logAdvancedUtil;
    }

    /**
     * 获取日志组件 --- 只用于info
     *
     * @return
     */
    private static zzq.zzqsimpleframeworklog.LogInfoBasicUtil getSystemInfoLogUtil() {
        Logger logger = LoggerFactory.getLogger(LogTypeEnum.SYSTEM_INFO.getLogTypeName());
        zzq.zzqsimpleframeworklog.LogInfoBasicUtil logInfoBasicUtil = new zzq.zzqsimpleframeworklog.LogInfoBasicUtil(logger);
        return logInfoBasicUtil;
    }

    /**
     * 获取日志组件 --- 只用于error
     *
     * @return
     */
    private static zzq.zzqsimpleframeworklog.LogErrorBasicUtil getSystemErrorLogUtil() {
        Logger logger = LoggerFactory.getLogger(LogTypeEnum.SYSTEM_ERROR.getLogTypeName());
        zzq.zzqsimpleframeworklog.LogErrorBasicUtil logErrorBasicUtil = new zzq.zzqsimpleframeworklog.LogErrorBasicUtil(logger);
        return logErrorBasicUtil;
    }

}
