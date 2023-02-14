package zzq.zzqsimpleframeworklog.exception;

/**
 * 日志自定义异常
 *
 * @author zhouzhiqiang
 * @version 1.0
 * @date 2023-01-13 18:23
 */
public class LogException extends RuntimeException{

    public LogException() {
    }

    public LogException(String message) {
        super(message);
    }

    public LogException(String message, Throwable cause) {
        super(message, cause);
    }

    public LogException(Throwable cause) {
        super(cause);
    }

    public LogException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
