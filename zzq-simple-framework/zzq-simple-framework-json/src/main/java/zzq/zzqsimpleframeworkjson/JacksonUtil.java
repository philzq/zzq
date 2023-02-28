package zzq.zzqsimpleframeworkjson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zzq.zzqsimpleframeworkjson.config.JacksonConfigure;

/**
 * @author zhouzhiqiang
 * @version 1.0
 * @date 2023-01-04 11:12
 */
public class JacksonUtil {

    private static final Logger logger = LoggerFactory.getLogger(JacksonUtil.class);

    /**
     * 配置ObjectMapper对象
     */
    private static final ObjectMapper objectMapper;

    static {
        objectMapper = new JacksonConfigure().objectMapper();
    }

    /**
     * 将对象转为string
     *
     * @param object
     * @return
     */
    public static String toJSon(Object object) {
        try {
            //Json对象转为String字符串
            return objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            logger.error("toJSon exception", e);
        }
        return null;
    }

    /**
     * 将对象解析为指定对象
     *
     * @param content
     * @param typeReference
     * @param <T>
     * @return
     */
    public static <T> T parseJson(String content, TypeReference<T> typeReference) {
        try {
            return objectMapper.readValue(content, typeReference);
        } catch (JsonProcessingException e) {
            logger.error("parseJson exception", e);
        }
        return null;
    }

    /**
     * 判断是不是一个json
     *
     * @param json
     * @return
     */
    public static boolean isJson(String json) {
        boolean valid = false;
        try {
            JsonParser parser = objectMapper.createParser(json);
            while (parser.nextToken() != null) {
            }
            valid = true;
        } catch (Exception e) {
            logger.error("isJson exception", e);
        }
        return valid;
    }
}
