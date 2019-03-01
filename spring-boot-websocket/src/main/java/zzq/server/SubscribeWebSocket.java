package zzq.server;

import java.util.concurrent.CopyOnWriteArraySet;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import zzq.config.ApplicationStartup;
import zzq.entity.SubscribeResponse;
import zzq.entity.enums.SubscribeType;

/**
 * 〈功能简述〉<br>
 * 〈对外提供订阅功能的WebSocket〉
 *
 * @create 2018/12/25
 */
@ServerEndpoint("/subscribeWebSocket/")
@Component
public class SubscribeWebSocket {

    //此处是解决无法注入的关键
    private static final Logger logger = LoggerFactory.getLogger(SubscribeWebSocket.class);

    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;

    //所有业务
    public static final CopyOnWriteArraySet<String> services = new CopyOnWriteArraySet<>();

    //所有客户端
    public static final CopyOnWriteArraySet<SubscribeWebSocket> clients = new CopyOnWriteArraySet<>();

    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    //订阅的业务,若为true表示第一次推送
    private CopyOnWriteArraySet<String> service = new CopyOnWriteArraySet<>();

    //心跳时间,长时间没心跳踢掉连接
    public long heartBeatTime;
    //初次连接时间，用于控制连接时间过长，踢掉连接
    public long beginTime;

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session) {
        addOnlineCount();
        logger.info("建立连接,当前连接数"+onlineCount);
        this.session = session;
        this.heartBeatTime = System.currentTimeMillis();
        this.beginTime = System.currentTimeMillis();
        clients.add(this);

    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        try {
            subOnlineCount(); //在线数减1
            this.session.close();
            logger.info("连接中断,当前连接数"+onlineCount);
            clients.remove(this);
        }catch (Exception e){
            logger.error("关闭连接出错 : {}" , e);
        }
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        logger.debug("收到消息:"+message);
        JSONObject jsonObject = JSONUtil.parseObj(message);
        String type = jsonObject.getStr("type", "errorType");
        if("HEART_BEAT".equals(type)){//心跳检测
            this.heartBeatTime = System.currentTimeMillis();
            sendMessage(JSONUtil.toJsonStr(new SubscribeResponse(200, "pong")));
            return;
        }
        if (!SubscribeType.contains(type)) {//不存在这个订阅类型
            sendMessage(JSONUtil.toJsonStr(new SubscribeResponse(500, "不存在此订阅类型")));
        }
        String service = jsonObject.getStr("service");
        if (service == null || !services.contains(service)) {//业务未空或者不存在
            sendMessage(JSONUtil.toJsonStr(new SubscribeResponse(500, "不存在此业务")));
            return;
        }
        //执行操作
        switch (type) {
            case "SUB"://订阅业务
                this.service.add(service);
                //控制初始化推送一次数据
                ApplicationStartup.cacheUtils.sendChannelMess(service,this.session.getId());
                sendMessage(JSONUtil.toJsonStr(new SubscribeResponse("SUB&"+service)));
                break;
            case "UN_SUB"://取消订阅
                this.service.remove(service);
                sendMessage(JSONUtil.toJsonStr(new SubscribeResponse( "UN_SUB&"+service)));
                break;
            default:
                break;
        }
    }

    /**
     * 发生错误时调用
     */
    @OnError
    public void onError(Session session, Throwable error) {
        logger.error("发生错误", error);
    }

    /**
     * 发送消息
     *
     * @param message
     */
    public void sendMessage(String message) {
        try {
            this.session.getAsyncRemote().sendText(message);//異步
        }catch (Exception e){
           logger.error("向客户端发送消息出错: {}", e);
        }
    }

    /**
     * 向指定客户端发送消息
     * @param service
     * @param message
     * @param sessionId
     */
    public static void sendMessageToOne(String service,String message,String sessionId){
        for(SubscribeWebSocket subscribeWebSocket : clients){
            if(subscribeWebSocket.session.getId().equals(sessionId)){
                if(subscribeWebSocket.service.contains(service)){
                    try {
                        subscribeWebSocket.session.getAsyncRemote().sendText(message);
                    }catch (Exception e){
                        logger.error("向客户端发送消息出错: {}", e);
                    }
                }
                return;
            }
        }
    }

    /**
     * 向订阅了此业务的客户端发送消息
     * @param service
     * @param message
     */
    public static void sendMessAgeAll(String service,String message) {
        for(SubscribeWebSocket subscribeWebSocket : clients){
            if(subscribeWebSocket.service.contains(service)){
                try {
                    subscribeWebSocket.session.getAsyncRemote().sendText(message);
                }catch (Exception e){
                	logger.error("向客户端发送消息出错: {}", e);
                }
            }
        }
    }

    /**
     * 添加业务
     * @param service
     */
    public static void addService(String service){
        if(!SubscribeWebSocket.services.contains(service)){
            SubscribeWebSocket.services.add(service);
        }
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        SubscribeWebSocket.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        SubscribeWebSocket.onlineCount--;
    }
}
