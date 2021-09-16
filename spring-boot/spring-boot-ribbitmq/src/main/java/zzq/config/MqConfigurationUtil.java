package zzq.config;

import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

public class MqConfigurationUtil {

    /**
     * 初始化mq监听容器工厂
     * @param factory
     */
    static void initSimpleRabbitListenerContainerFactory(SimpleRabbitListenerContainerFactory factory) {
        //手动应答
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        //当消费消息抛出异常没有catch住时，这条消息不会被rabbitmq放回到queue头部，再被推送过来，防止死循环
        factory.setDefaultRequeueRejected(false);
        //预取数量
        factory.setPrefetchCount(25);
        //设置超时
        factory.setReceiveTimeout(2000L);
        //设置重试间隔
        factory.setFailedDeclarationRetryInterval(3000L);
    }

    /**
     * 初始化连接工厂
     * @param connectionFactory
     */
    static void initMqConnectionFactory(CachingConnectionFactory connectionFactory) {
        connectionFactory.setPublisherConfirms(true);
        connectionFactory.setPublisherReturns(true);
    }

    /**
     * 初始化RabbitTemplate
     * @param secondRabbitTemplate
     */
    static void initRabbitTemplate(RabbitTemplate secondRabbitTemplate) {
        //代表强制发送，即使exchange找不到routingkey对应的队列也会尝试，如果失败则会回调ReturnCallback
        secondRabbitTemplate.setMandatory(true);
        secondRabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            System.out.println("=====secondRabbitTemplate消息进行消费了======");
            if (ack) {
                System.out.println("消息id为: " + correlationData + "的消息，已经被ack成功");
            } else {
                System.out.println("消息id为: " + correlationData + "的消息，消息nack，失败原因是：" + cause);
            }
        });
        secondRabbitTemplate.setReturnCallback((message, replyCode, replyText, exchange, routingKey) -> {
            System.out.println("secondRabbitTemplate***setReturnCallback*********************");
            System.out.println("消息主体 message：" + message);
            System.out.println("应答码 replyCode: ：" + replyCode);
            System.out.println("原因描述 replyText：" + replyText);
            System.out.println("交换机 exchange：" + exchange);
            System.out.println("消息使用的路由键 routingKey：" + routingKey);
        });
    }
}
