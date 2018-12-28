package zzq.service.subscription;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 〈功能简述〉<br>
 * 〈消息订阅基础类〉
 *
 * @author zhouzhiqiang
 * @create 2018-12-28
 */
@Component
public interface BaseSub {

    /**
     * 接收消息
     * @param message
     */
    void receiveMessage(String message);
}
