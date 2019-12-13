package zzq.nio.http.server;

import java.util.Map;

/**
 * Request
 *
 */
public class Request {

    /**
     * method
     */
    private String method;

    /**
     * http协议版本
     */
    private String httpVersion;

    /**
     * 请求uri
     */
    private String uri;

    /**
     * 请求相对路径
     */
    private String path;


    /**
     * 请求头信息
     */
    private Map<String, String> headers;

    /**
     * 请求参数
     */
    private Map<String, String> attribute;


    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getHttpVersion() {
        return httpVersion;
    }

    public void setHttpVersion(String httpVersion) {
        this.httpVersion = httpVersion;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public Map<String, String> getAttribute() {
        return attribute;
    }

    public void setAttribute(Map<String, String> attribute) {
        this.attribute = attribute;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
