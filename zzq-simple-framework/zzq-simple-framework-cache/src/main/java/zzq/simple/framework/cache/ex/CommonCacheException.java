package zzq.simple.framework.cache.ex;

/**
 * htpclient自定义异常
 *
 * @author zhouzhiqiang
 * @version 1.0
 * @date 2023-01-13 18:23
 */
public class CommonCacheException extends RuntimeException{

    public CommonCacheException() {
    }

    public CommonCacheException(String message) {
        super(message);
    }

    public CommonCacheException(String message, Throwable cause) {
        super(message, cause);
    }

    public CommonCacheException(Throwable cause) {
        super(cause);
    }

    public CommonCacheException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
