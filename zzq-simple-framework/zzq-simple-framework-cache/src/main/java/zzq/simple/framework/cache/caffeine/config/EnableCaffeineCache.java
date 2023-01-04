package zzq.simple.framework.cache.caffeine.config;

import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * caffeineCache生效类
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
@Import({CaffeineLoadingCacheConfig.class})
public @interface EnableCaffeineCache {
}
