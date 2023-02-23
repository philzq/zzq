package zzq.zzqsimpleframeworklog;

import org.slf4j.helpers.MessageFormatter;
import zzq.zzqsimpleframeworkjson.JacksonUtil;
import zzq.zzqsimpleframeworklog.entity.SystemInfoLogEntity;
import zzq.zzqsimpleframeworklog.util.LogInitUtil;
import org.slf4j.Logger;

/**
 * 日志工具类  -- 基础的使用，，只有SystemInfo和SystemError应用场景
 *
 * @author zhouzhiqiang
 * @version 1.0
 * @date 2023-02-09 10:19
 */
public class LogInfoBasicUtil {

    private final Logger logger;

    public LogInfoBasicUtil(Logger logger) {
        this.logger = logger;
    }

    /*******************************info*********************************/
    public void info(String title, String format, Object... args) {
        String msg = MessageFormatter.arrayFormat(format, args).getMessage();
        SystemInfoLogEntity systemInfoLogEntity = SystemInfoLogEntity.builder()
                .title(title)
                .message(msg)
                .build();
        info(systemInfoLogEntity);
    }

    public void info(SystemInfoLogEntity systemInfoLogEntity) {
        //初始化通用字段
        LogInitUtil.initCommonFields(systemInfoLogEntity);
        logger.info(JacksonUtil.toJSon(systemInfoLogEntity));
    }
    /*******************************info*********************************/
}
