package zzq.zzqsimpleframeworklog.config;


import zzq.zzqsimpleframeworkcommon.util.IpUtils;
import zzq.zzqsimpleframeworklog.exception.LogException;

/**
 * 系统常量
 *
 * @author zhouzhiqiang
 * @version 1.0
 * @date 2023-02-10 14:41
 */
public class SystemConstant {

    /**
     * 应用名称
     */
    public static final String APP_NAME;

    /**
     * 主机ip
     */
    public static final String HOST_IP;

    /**
     * 项目包路径
     */
    public static final String PROJECT_PACKAGE = "zzq.zzqsimpleframeworklog";

    static {
        APP_NAME = System.getProperty("appName");
        if (APP_NAME == null) {
            throw new LogException("System.getProperty(\"appName\")不能为null");
        }
        HOST_IP = IpUtils.getHostIp();
    }
}
