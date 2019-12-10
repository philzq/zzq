package zzq.service.subscription;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 〈功能简述〉<br>
 * 〈心跳监听检测〉
 *
 * @author zhouzhiqiang
 * @create 2018-12-28
 */
@Component
@Slf4j
public class HeartBeatSub implements BaseSub{

    @Override
    public void receiveMessage(String message) {
        log.info(message);
    }
}
