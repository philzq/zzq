package zzq.zzqsimpleframeworkcommon.util;

import zzq.zzqsimpleframeworkcommon.entity.ProjectConstant;
import zzq.zzqsimpleframeworkcommon.exception.CustomUncaughtExceptionHandler;
import zzq.zzqsimpleframeworkcommon.exception.GeneralException;
import org.apache.commons.lang3.StringUtils;

import java.util.TimeZone;

/**
 * 项目工具类
 *
 * @author zhouzhiqiang
 * @version 1.0
 * @date 2023-02-16 13:36
 */
public class ProjectUtil {

    /**
     * 获取应用名称
     *
     * @return
     */
    public static String getAppName() {
        return System.getProperty(ProjectConstant.APP_NAME_KEY);
    }

    /**
     * 启动初始化
     */
    public static void startInit(String appName) {
        if (StringUtils.isEmpty(appName)) {
            throw new GeneralException("startInit初始化appName不能为空");
        }
        System.setProperty(ProjectConstant.APP_NAME_KEY, appName);
        //线程异常全局捕获
        Thread.setDefaultUncaughtExceptionHandler(new CustomUncaughtExceptionHandler());
        //设置项目时区为东八区
        TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));
    }
}
