package zzq.nio.http.server.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * CloudHttpConfig
 *
 */
public class CloudHttpConfig {

    private final static Logger logger = LoggerFactory.getLogger(CloudHttpConfig.class);

    /**
     * 服务器配置信息
     */
    private static Properties properties;

    /**
     * 根据key获取配置项
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public static String getValue(String key,String defaultValue){
        ensureProps();
        String value = properties.getProperty(key,defaultValue);
        return value;
    }

    /**
     * 根据key获取配置项
     *
     * @param key
     * @return
     */
    public static String getValue(String key) {
        ensureProps();
        String value = properties.getProperty(key);
        return value;
    }

    private static void ensureProps() {
        if (properties == null) {
            InputStream inputStream = CloudHttpConfig.class.getClassLoader().getResourceAsStream("server.properties");
            properties = new Properties();
            try {
                properties.load(inputStream);
            } catch (IOException e) {
                logger.error("error_CloudHttpConfig_ensureProps, 异常", e);
            }
        }
    }
}
