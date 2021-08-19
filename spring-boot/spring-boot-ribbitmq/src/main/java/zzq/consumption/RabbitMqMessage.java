package zzq.consumption;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class RabbitMqMessage {

    @RabbitListener(queues = "testFirst", containerFactory = "firstFactory")
    public void testFirst(Channel channel, Message message) throws IOException {
        try {
            System.out.println(LocalDateTime.now() + "-firstFactory:" + new String(message.getBody()));
            Thread.sleep(10);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
        }
    }

    @RabbitListener(queues = "testSecond", containerFactory = "secondFactory")
    public void testSecond(Channel channel, Message message) throws IOException {
        try {
            System.out.println(LocalDateTime.now() + "-secondFactory:" + new String(message.getBody()));
            Thread.sleep(10);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
        }
    }
}

