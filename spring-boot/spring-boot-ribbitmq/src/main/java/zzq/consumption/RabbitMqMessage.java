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
            System.out.println(1 / 0);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
            e.printStackTrace();
        }
    }

    @RabbitListener(queues = "testSecond", containerFactory = "secondFactory")
    public void testSecond(Channel channel, Message message) throws IOException {
        try {
            System.out.println(LocalDateTime.now() + "-secondFactory:" + new String(message.getBody()));
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
            e.printStackTrace();
        }
    }
}

