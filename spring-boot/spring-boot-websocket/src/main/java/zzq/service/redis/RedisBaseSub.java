package zzq.service.redis;

import org.springframework.stereotype.Component;

/**
 * 〈功能简述〉<br>
 * 〈redis接收消息基础类〉
 *
 * @create 2018/12/27
 */
@Component
public interface RedisBaseSub {

    /**
     * 接收消息
     * @param message
     */
    void receiveMessage(String message);
}
