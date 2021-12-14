package zzq.production;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class Receiver {

    @Resource(name = "firstRabbitTemplate")
    private RabbitTemplate firstRabbitTemplate;

    @Autowired
    private Sender sender;

    @Resource(name = "secondRabbitTemplate")
    private RabbitTemplate secondRabbitTemplate;

    public String testFirstReceive(String testException) {
        String result = null;
        Message testFirst = null;
        try {
            testFirst = firstRabbitTemplate.receive("testFirst");
            if (testFirst != null) {
                result = new String(testFirst.getBody());
            }
            Integer.parseInt(testException);
        } catch (Exception e) {
            //神补偿机制
            //记录日志（这个记录日志至于要做什么用看业务）
            // 再重新发送到mq
            // 当然为了避免死循环，这里可以实现为发到另一个队列，主动消费而非被动订阅来处理
            if (result != null) {
                sender.testFirst(result);
            }
        }

        return result;
    }
}
