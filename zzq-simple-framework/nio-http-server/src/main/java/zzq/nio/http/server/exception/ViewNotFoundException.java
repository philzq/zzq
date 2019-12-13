package zzq.nio.http.server.exception;

/**
 * ViewNotFoundException
 *
 */
public class ViewNotFoundException extends RuntimeException {

    public ViewNotFoundException() {
        super("404 页面丢失了！！");
    }
}
