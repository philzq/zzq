package zzq.consumption;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class RabbitMqMessage {

    @RabbitListener(queues = "testFirst", containerFactory="firstFactory")
    public void testFirst(Object data) {
        try {
            System.out.println("firstFactory:"+ data);
            Thread.sleep(10);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @RabbitListener(queues = "testSecond", containerFactory="secondFactory")
    public void testSecond(Object data) {
        try {
            System.out.println("secondFactory:"+ data);
            Thread.sleep(10);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

