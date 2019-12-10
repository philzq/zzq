package zzq.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.interceptor.SimpleCacheErrorHandler;
import org.springframework.cache.interceptor.SimpleKeyGenerator;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import zzq.entity.enums.Caches;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfig extends CachingConfigurerSupport {
    @Bean // important!
    @Override
    public CacheManager cacheManager() {
        // configure and return an implementation of Spring's CacheManager SPI
        SimpleCacheManager manager = new SimpleCacheManager();
        ArrayList<CaffeineCache> caches = new ArrayList<>();
        for (Caches cache : Caches.values()) {
            caches.add(new CaffeineCache(cache.name(),
                    Caffeine.newBuilder().recordStats()
                            .expireAfterWrite(cache.gettimeOut(), TimeUnit.SECONDS)
                            .maximumSize(cache.getMaxSize())
                            .build())
            );
        }
        manager.setCaches(caches);
        return manager;
    }

    @Bean // important!
    @Override
    public KeyGenerator keyGenerator() {
        // configure and return KeyGenerator instance
        return new SimpleKeyGenerator();
    }

    @Bean // important!
    @Override
    public CacheErrorHandler errorHandler() {
        // configure and return CacheErrorHandler instance
        return new SimpleCacheErrorHandler();
    }


}
