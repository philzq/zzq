package zzq.annotation;

import org.springframework.context.annotation.Import;
import zzq.utils.RedisUtils;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import(RedisUtils.class)
public @interface EnableRedisUtils {
}
