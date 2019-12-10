package zzq.simple.spring.framework.helper;


import zzq.simple.spring.framework.ConfigConstant;
import zzq.simple.spring.framework.util.PropsUtil;

import java.util.Properties;

/**
 * 属性文件助手类
 * 该类用于获取配置文件中的配置, 其中JSP和静态资源设置了默认路径
 */
public final class ConfigHelper {

    /**
     * 加载配置文件的属性
     */
    private static final Properties CONFIG_PROPS = PropsUtil.loadProps(ConfigConstant.CONFIG_FILE);


    /**
     * 获取应用基础包名
     */
    public static String getAppBasePackage() {
        return PropsUtil.getString(CONFIG_PROPS, ConfigConstant.APP_BASE_PACKAGE);
    }

    /**
     * 根据属性名获取 String 类型的属性值
     */
    public static String getString(String key) {
        return PropsUtil.getString(CONFIG_PROPS, key);
    }

    /**
     * 根据属性名获取 int 类型的属性值
     */
    public static int getInt(String key) {
        return PropsUtil.getInt(CONFIG_PROPS, key);
    }

    /**
     * 根据属性名获取 boolean 类型的属性值
     */
    public static boolean getBoolean(String key) {
        return PropsUtil.getBoolean(CONFIG_PROPS, key);
    }
}
