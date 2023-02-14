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

    LogInfoBasicUtil SYSTEM_INFO = zzq.zzqsimpleframeworklog.LogUtilFactory.getSystemInfoLogUtil();
    LogErrorBasicUtil SYSTEM_ERROR = zzq.zzqsimpleframeworklog.LogUtilFactory.getSystemErrorLogUtil();

    LogAdvancedUtil<WebDigestLogEntity> WEB_DIGEST = zzq.zzqsimpleframeworklog.LogUtilFactory.getLogUtil(LogTypeEnum.WEB_DIGEST, WebDigestLogEntity.class);
    LogAdvancedUtil<AccountMonitorLogEntity> ACCOUNT_MONITOR = zzq.zzqsimpleframeworklog.LogUtilFactory.getLogUtil(LogTypeEnum.ACCOUNT_MONITOR, AccountMonitorLogEntity.class);
    LogAdvancedUtil<ApiMonitorLogEntity> API_MONITOR = zzq.zzqsimpleframeworklog.LogUtilFactory.getLogUtil(LogTypeEnum.API_MONITOR, ApiMonitorLogEntity.class);
    LogAdvancedUtil<KafkaConsumerLogEntity> KAFKA_CONSUMER = zzq.zzqsimpleframeworklog.LogUtilFactory.getLogUtil(LogTypeEnum.KAFKA_CONSUMER, KafkaConsumerLogEntity.class);
    LogAdvancedUtil<MongoLogEntity> MONGO = zzq.zzqsimpleframeworklog.LogUtilFactory.getLogUtil(LogTypeEnum.MONGO, MongoLogEntity.class);
    LogAdvancedUtil<OrderMonitorLogEntity> ORDER_MONITOR = zzq.zzqsimpleframeworklog.LogUtilFactory.getLogUtil(LogTypeEnum.ORDER_MONITOR, OrderMonitorLogEntity.class);
    LogAdvancedUtil<PullingMonitorLogEntity> PULLING_MONITOR = zzq.zzqsimpleframeworklog.LogUtilFactory.getLogUtil(LogTypeEnum.PULLING_MONITOR, PullingMonitorLogEntity.class);
    LogAdvancedUtil<PushMonitorLogEntity> PUSH_MONITOR = zzq.zzqsimpleframeworklog.LogUtilFactory.getLogUtil(LogTypeEnum.PUSH_MONITOR, PushMonitorLogEntity.class);
    LogAdvancedUtil<PushTimelyMonitorLogEntity> PUSHTIMELY_MONITOR = zzq.zzqsimpleframeworklog.LogUtilFactory.getLogUtil(LogTypeEnum.PUSHTIMELY_MONITOR, PushTimelyMonitorLogEntity.class);
    LogAdvancedUtil<ReceptionMonitorLogEntity> RECEPTION_MONITOR = zzq.zzqsimpleframeworklog.LogUtilFactory.getLogUtil(LogTypeEnum.RECEPTION_MONITOR, ReceptionMonitorLogEntity.class);
    LogAdvancedUtil<RemoteDigestLogEntity> REMOTE_DIGEST = zzq.zzqsimpleframeworklog.LogUtilFactory.getLogUtil(LogTypeEnum.REMOTE_DIGEST, RemoteDigestLogEntity.class);
    LogAdvancedUtil<RpcDigestLogEntity> RPC_DIGEST = zzq.zzqsimpleframeworklog.LogUtilFactory.getLogUtil(LogTypeEnum.RPC_DIGEST, RpcDigestLogEntity.class);
    LogAdvancedUtil<TimelyMonitorLogEntity> TIMELY_MONITOR = zzq.zzqsimpleframeworklog.LogUtilFactory.getLogUtil(LogTypeEnum.TIMELY_MONITOR, TimelyMonitorLogEntity.class);
    LogAdvancedUtil<RoutingMonitorLogEntity> ROUTING_MONITOR = zzq.zzqsimpleframeworklog.LogUtilFactory.getLogUtil(LogTypeEnum.ROUTING_MONITOR, RoutingMonitorLogEntity.class);


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
    static <T extends BaseLogEntity> LogAdvancedUtil<T> getLogUtil(LogTypeEnum logTypeEnum, Class<T> t) {
        Logger logger = LoggerFactory.getLogger(logTypeEnum.getLogTypeName());
        LogAdvancedUtil<T> logAdvancedUtil = new LogAdvancedUtil<>(logger, t);
        return logAdvancedUtil;
    }

    /**
     * 获取日志组件 --- 只用于info
     *
     * @return
     */
    static LogInfoBasicUtil getSystemInfoLogUtil() {
        Logger logger = LoggerFactory.getLogger(LogTypeEnum.SYSTEM_INFO.getLogTypeName());
        LogInfoBasicUtil logInfoBasicUtil = new LogInfoBasicUtil(logger);
        return logInfoBasicUtil;
    }

    /**
     * 获取日志组件 --- 只用于error
     *
     * @return
     */
    static LogErrorBasicUtil getSystemErrorLogUtil() {
        Logger logger = LoggerFactory.getLogger(LogTypeEnum.SYSTEM_ERROR.getLogTypeName());
        LogErrorBasicUtil logErrorBasicUtil = new LogErrorBasicUtil(logger);
        return logErrorBasicUtil;
    }

}
