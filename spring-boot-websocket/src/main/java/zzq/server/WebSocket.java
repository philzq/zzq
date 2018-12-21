package zzq.server;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * 〈功能简述〉<br>
 * 〈WebSocketServer〉
 *
 * @author zhouzhiqiang
 * @create 2018-12-21
 */
@ServerEndpoint("/websocket/{username}")
@Component
public class WebSocket {

    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;

    //根据名字存储websocket对象CopyOnWriteArraySet线程安全set，ConcurrentHashMap线程安全map
    public static Map<String, CopyOnWriteArraySet<WebSocket>> webSocketMap = new ConcurrentHashMap<>();

    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    /**
     * 用户名称
     */
    private String username;

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(@PathParam("username") String username, Session session) {
        this.username = username;
        this.session = session;
        //将用户添加到websocket，支持单用户多出链接
        if (webSocketMap.containsKey(username)) {
            webSocketMap.get(username).add(this);
        } else {
            CopyOnWriteArraySet websocketSet = new CopyOnWriteArraySet();
            websocketSet.add(this);
            webSocketMap.put(username, websocketSet);
            addOnlineCount();           //在线数加1
            System.out.println("有新连接加入！当前在线人数为" + getOnlineCount());
        }
        Map<String, Object> messageMap = new ConcurrentHashMap<>();
        messageMap.put("type", 0);
        messageMap.put("message", username + "加入！当前在线人数为" + getOnlineCount());
        messageMap.put("users", webSocketMap.keySet());
        sendMessageAll(JSONUtil.toJsonStr(messageMap));
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        if (StringUtils.isNotEmpty(this.username)) {
            webSocketMap.get(username).remove(this);//删除链接
            if(webSocketMap.get(username).size()==0){
                webSocketMap.remove(username);
                subOnlineCount(); //在线数减1
                System.out.println("有一连接关闭！当前在线人数为" + getOnlineCount());
                //刷新用户列表
                Map<String, Object> messageMap = new ConcurrentHashMap<>();
                messageMap.put("type", 0);
                messageMap.put("message", username + "退出！当前在线人数为" + getOnlineCount());
                messageMap.put("users", webSocketMap.keySet());
                sendMessageAll(JSONUtil.toJsonStr(messageMap));
            }
        }
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("来自客户端的消息:" + message);
        //群发消息
        JSONObject messageJson = JSONUtil.parseObj(message);
        Object toUser = messageJson.get("to");//接收对象
        if ("All".equals(toUser)) {
            sendMessageAll(message);
        } else {
            sendMessageOne(message, toUser + "");
        }
    }

    /**
     * 发生错误时调用
     */
    @OnError
    public void onError(Session session, Throwable error) {
        System.out.println("发生错误");
        error.printStackTrace();
    }

    /**
     * 发送消息
     *
     * @param message
     * @throws IOException
     */
    public void sendMessage(String message) throws IOException {
        //this.session.getBasicRemote().sendText(message);//同步
        this.session.getAsyncRemote().sendText(message);//異步
    }

    /**
     * 发送消息给指定用户
     *
     * @param message
     * @param toUserName
     */
    public void sendMessageOne(String message, String toUserName) {
        for (String key : webSocketMap.keySet()) {
            if (key.equals(toUserName)) {
                for (WebSocket websocket : webSocketMap.get(toUserName)) {
                    websocket.session.getAsyncRemote().sendText(message);
                }
                break;
            }
        }
    }

    /**
     * 发送消息给所有用户
     *
     * @param message
     * @throws IOException
     */
    public void sendMessageAll(String message) {
        for (String key : webSocketMap.keySet()) {
            for (WebSocket websocket : webSocketMap.get(key)) {
                websocket.session.getAsyncRemote().sendText(message);
            }
        }
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WebSocket.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        WebSocket.onlineCount--;
    }
}
