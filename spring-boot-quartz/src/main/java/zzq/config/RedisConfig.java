package zzq.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import zzq.service.subscription.BaseSub;
import zzq.service.subscription.HeartBeatSub;

/**
 * 〈功能简述〉<br>
 * 〈redis消息订阅〉
 *
 * @author zhouzhiqiang
 * @create 2018-12-28
 */
@Configuration
public class RedisConfig {

    /**
     * redis消息监听器容器
     * 可以添加多个监听不同话题的redis监听器，只需要把消息监听器和相应的消息订阅处理器绑定，该消息监听器
     * 通过反射技术调用消息订阅处理器的相关方法进行一些业务处理
     * @param connectionFactory
     * @param heartBeatSub
     * @return
     */
    @Bean
    RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory,
                                            HeartBeatSub heartBeatSub) {

        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        //订阅了一个叫chat 的通道
        container.addMessageListener(listenerAdapter(heartBeatSub), new PatternTopic("heartBeatSub"));
        //这个container 可以添加多个 messageListener
        return container;
    }

    /**
     * 消息监听器适配器，绑定消息处理器，利用反射技术调用消息处理器的业务方法
     * @param baseSub
     * @return
     */
    @Bean
    MessageListenerAdapter listenerAdapter(BaseSub baseSub) {
        //这个地方 是给messageListenerAdapter 传入一个消息接受的处理器，利用反射的方法调用“receiveMessage”
        //也有好几个重载方法，这边默认调用处理器的方法 叫handleMessage 可以自己到源码里面看
        return new MessageListenerAdapter(baseSub, "receiveMessage");
    }
}
