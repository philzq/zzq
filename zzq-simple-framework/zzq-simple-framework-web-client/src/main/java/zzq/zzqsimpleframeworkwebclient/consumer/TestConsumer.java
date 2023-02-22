package zzq.zzqsimpleframeworkwebclient.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import zzq.zzqsimpleframeworkwebclient.entity.KafkaConstant;

/**
 * 测试kafka消费
 *
 * @author zhouzhiqiang
 * @version 1.0
 * @date 2023-02-17 10:20
 */
@Component
public class TestConsumer {

    // 消费监听
    @KafkaListener(topics = {KafkaConstant.TEST_TOPIC})
    public void onMessage(ConsumerRecord<?, ?> record, Acknowledgment acknowledgment) {
        System.out.println("简单消费："+record.topic()+"-"+record.partition()+"-"+record.value());
        acknowledgment.acknowledge();
    }
}
