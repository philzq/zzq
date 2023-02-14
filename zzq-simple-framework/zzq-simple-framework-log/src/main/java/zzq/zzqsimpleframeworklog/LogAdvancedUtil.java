package zzq.zzqsimpleframeworklog;

import zzq.zzqsimpleframeworkjson.JacksonUtil;
import zzq.zzqsimpleframeworklog.entity.common.BaseLogEntity;
import zzq.zzqsimpleframeworklog.util.LogInitUtil;
import org.slf4j.Logger;

/**
 * 日志工具类
 *
 * @author zhouzhiqiang
 * @version 1.0
 * @date 2023-02-09 10:19
 */
public class LogAdvancedUtil<T extends BaseLogEntity> {

    private final Logger logger;

    public LogAdvancedUtil(Logger logger, Class<T> t) {
        this.logger = logger;
    }

    /*******************************info*********************************/
    public void info(T t) {
        //初始化通用字段
        LogInitUtil.initCommonFields(t);
        logger.info(JacksonUtil.toJSon(t));
    }
    /*******************************info*********************************/
}
