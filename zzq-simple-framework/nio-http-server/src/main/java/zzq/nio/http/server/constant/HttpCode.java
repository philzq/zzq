package zzq.nio.http.server.constant;

/**
 * HttpCode
 *
 */
public enum HttpCode {

    STATUS_200(200, "OK"),
    STATUS_400(400, "Bad Request"),
    STATUS_403(403, "Forbidden"),
    STATUS_404(404, "Not Found"),

    /**
     * 永久重定向
     */
    STATUS_301(301, "Moved Permanently"),

    /**
     * 临时重定向
     */
    STATUS_302(302, "Found"),
    STATUS_500(500, "Internal Server Error");

    HttpCode(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private Integer code;

    private String msg;

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

}
