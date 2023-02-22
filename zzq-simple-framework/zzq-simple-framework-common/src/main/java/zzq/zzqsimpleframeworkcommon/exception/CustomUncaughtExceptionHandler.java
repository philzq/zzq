package zzq.zzqsimpleframeworkcommon.exception;

import zzq.zzqsimpleframeworkcommon.context.ThreadLocalManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 线程异常就删除线程副本，自定义异常处理
 * 通过Thread.setDefaultUncaughtExceptionHandler(new CustomUncaughtExceptionHandler());使用
 *
 * @author zhouzhiqiang
 * @version 1.0
 * @date 2023-02-15 10:58
 */
public class CustomUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(CustomUncaughtExceptionHandler.class);

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        ThreadLocalManager.clear();
        logger.error(t.getName() + "线程异常", e);
    }
}
