package zzq.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import zzq.service.redis.HeartBeatSub;
import zzq.service.redis.RedisBaseSub;

/**
 * 〈功能简述〉<br>
 * 〈搭建redis消息订阅〉
 *
 * @create 2018/12/27
 */
@Configuration
@EnableCaching
public class RedisConfig {

    @Bean
    public RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory,
                                                   HeartBeatSub heartBeatSub){
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        //订阅了一个叫trafficRecord 的通道
        container.addMessageListener(listenerAdapter(heartBeatSub),new PatternTopic("heartBeat"));
        //这个container 可以添加多个 messageListener
        return container;
    }

    /**
     * 绑定消息监听者和接收监听的方法,必须要注入这个监听器，不然会报错
     */
    @Bean
    @Scope("prototype")
    public MessageListenerAdapter listenerAdapter(RedisBaseSub redisBaseSub){
        //这个地方 是给messageListenerAdapter 传入一个消息接受的处理器，利用反射的方法调用“receiveMessage”
        //也有好几个重载方法，这边默认调用处理器的方法 叫handleMessage 可以自己到源码里面看
        return new MessageListenerAdapter(redisBaseSub,"receiveMessage");
    }
}
