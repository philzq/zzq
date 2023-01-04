package zzq.zzqsimpleframeworkjson.config;

import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.context.annotation.Import;
import zzq.zzqsimpleframeworkjson.JacksonUtil;

import java.lang.annotation.*;

/**
 * 集成spring，统一json的序列化与反序列化及使用
 *
 * @author zhouzhiqiang
 * @version 1.0
 * @date 2023-01-04 10:15
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@AutoConfigurationPackage
@Import({JacksonConfigure.class, JacksonUtil.class})
public @interface EnableJackson {
}
