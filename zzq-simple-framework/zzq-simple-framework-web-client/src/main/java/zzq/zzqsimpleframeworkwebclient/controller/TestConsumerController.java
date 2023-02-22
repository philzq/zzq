package zzq.zzqsimpleframeworkwebclient.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zzq.zzqsimpleframeworkwebclient.entity.KafkaConstant;

/**
 * 测试kafka发送
 *
 * @author zhouzhiqiang
 * @version 1.0
 * @date 2023-02-17 10:21
 */
@RestController
@RequestMapping("testConsumer")
public class TestConsumerController {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @GetMapping("sendToTestTopic")
    public String sendToTestTopic(String msg) {
        kafkaTemplate.send(KafkaConstant.TEST_TOPIC,0,"zzzq-test-key", msg);
        return "success:" + msg;
    }


}
