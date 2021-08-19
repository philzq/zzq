package zzq.consumption;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class RabbitMqMessage {

    @RabbitListener(queues = "testFirst", containerFactory="firstFactory")
    public void testFirst(Object data) {
        try {
            System.out.println(LocalDateTime.now()+"-firstFactory:"+ data);
            Thread.sleep(10);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @RabbitListener(queues = "testSecond", containerFactory="secondFactory")
    public void testSecond(Object data) {
        try {
            System.out.println(LocalDateTime.now()+"-secondFactory:"+ data);
            Thread.sleep(10);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

