package zzq.zzqsimpleframeworkhttp.exception;

/**
 * htpclient自定义异常
 *
 * @author zhouzhiqiang
 * @version 1.0
 * @date 2023-01-13 18:23
 */
public class HttpClientException extends RuntimeException{

    public HttpClientException() {
    }

    public HttpClientException(String message) {
        super(message);
    }

    public HttpClientException(String message, Throwable cause) {
        super(message, cause);
    }

    public HttpClientException(Throwable cause) {
        super(cause);
    }

    public HttpClientException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
