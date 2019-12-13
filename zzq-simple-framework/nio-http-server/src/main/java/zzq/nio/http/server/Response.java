package zzq.nio.http.server;

import zzq.nio.http.server.constant.HttpCode;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Response
 */
public class Response {

    private String htm;

    private Integer code;

    private String protocol = "HTTP/1.1";

    private String msg;

    private Map<String, String> headers;

    private ByteArrayOutputStream outPutStream = new ByteArrayOutputStream();


    public Response() {
        this.code = HttpCode.STATUS_200.getCode();
        this.msg = HttpCode.STATUS_200.getMsg();
        headers = new HashMap<>();
        headers.put("Content-Type", "text/html;charset=UTF-8");
        headers.put("Server", "cloud http v1.0");
        //headers.put("Content-Length", String.valueOf(htm.getBytes().length));
    }

    public String getHtm() {
        return htm;
    }

    public void setHtm(String htm) {
        this.htm = htm;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public ByteArrayOutputStream getOutPutStream() {
        return outPutStream;
    }

    public void setOutPutStream(ByteArrayOutputStream outPutStream) {
        this.outPutStream = outPutStream;
    }
}

