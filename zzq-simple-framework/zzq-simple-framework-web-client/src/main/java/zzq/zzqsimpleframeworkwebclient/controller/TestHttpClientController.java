package zzq.zzqsimpleframeworkwebclient.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zzq.zzqsimpleframeworkwebclient.entity.KafkaConstant;
import zzq.zzqsimpleframeworkwebclient.test.TestHttpClientService;

/**
 * @author zhouzhiqiang
 * @version 1.0
 * @date 2023-01-09 13:56
 */
@RestController
@RequestMapping("testHttpClient")
public class TestHttpClientController {

    @Autowired
    private TestHttpClientService testHttpClientService;

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @GetMapping("getOK")
    public String getOK() {
        String ok = testHttpClientService.getOK();
        return "zzq" + ok;
    }

    @GetMapping("testOk")
    public String testOk(String asd) {
        kafkaTemplate.send(KafkaConstant.TEST_TOPIC,0,"zzzq-test-key", asd);
        //System.out.println("testOkhahaha:" + Thread.currentThread().getId());
        return "zzqtest" + Thread.currentThread().getId();
    }


}
