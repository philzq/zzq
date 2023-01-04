package zzq.zzqsimpleframeworkjson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private ObjectMapper objectMapper;

    public JacksonUtil(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * 将对象转为string
     *
     * @param object
     * @return
     */
    public String toJSon(Object object) {
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
    public <T> T parseJson(String content, TypeReference<T> typeReference) {
        try {
            return objectMapper.readValue(content, typeReference);
        } catch (JsonProcessingException e) {
            logger.error("parseJson exception", e);
        }
        return null;
    }
}
