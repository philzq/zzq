package zzq.production;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class Sender {

    @Resource(name="firstRabbitTemplate")
    private RabbitTemplate firstRabbitTemplate;

    @Resource(name="secondRabbitTemplate")
    private RabbitTemplate secondRabbitTemplate;

    public void testFirst(String message) {
        System.out.println("first : " + message);
        firstRabbitTemplate.convertAndSend("testFirst", "testFirst : " + message);
    }

    public void testSecond(String message) {
        System.out.println("second : " + message);
        secondRabbitTemplate.convertAndSend("testSecond", "testSecond : " + message);
    }

}
