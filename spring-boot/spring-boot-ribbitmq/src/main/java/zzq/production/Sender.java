package zzq.production;

import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.UUID;

@Component
public class Sender {

    @Resource(name = "firstRabbitTemplate")
    private RabbitTemplate firstRabbitTemplate;

    @Resource(name = "secondRabbitTemplate")
    private RabbitTemplate secondRabbitTemplate;

    public void testFirst(String message) {
        System.out.println("first : " + message);
        firstRabbitTemplate.convertAndSend("testFirst", (Object) ("testFirst : " + message), new CorrelationData(UUID.randomUUID().toString()));
    }

    public void testReturnCallback(String message) {
        System.out.println("first : " + message);
        firstRabbitTemplate.convertAndSend("testFirst-NO_ROUTE", (Object) ("testFirst : " + message), new CorrelationData(UUID.randomUUID().toString()));
    }

    public void testSecond(String message) {
        System.out.println("second : " + message);
        secondRabbitTemplate.convertAndSend("testSecond", (Object) ("testSecond : " + message), new CorrelationData(UUID.randomUUID().toString()));
    }

}
