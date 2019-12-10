package zzq.entity;

/**
 * 〈功能简述〉<br>
 * 〈订阅websocket响应实体类〉
 *
 * @create 2018/12/25
 */
public class SubscribeResponse{
    private static final long serialVersionUID = 1L;

    //订阅业务
    private String service;
    //响应状态
    private Integer status;
    //响应描述
    private String message;
    //响应时间
    private long time;
    //数据
    private Object data;

    public SubscribeResponse() {
        this.time = System.currentTimeMillis();
        this.status = 200;
        this.message = "操作成功";
    }

    public SubscribeResponse(Integer status, String message) {
        this.time = System.currentTimeMillis();
        this.status = status;
        this.message = message;
    }

    public SubscribeResponse(String service) {
        this();
        this.service = service;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
