package zzq.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zzq.production.Receiver;

@RestController
@RequestMapping("receiver")
public class RibbitMqReceiverController {

    @Autowired
    private Receiver receiver;

    @GetMapping("testFirst")
    public String testFirst(String testException) {
        String testFirstReceive = receiver.testFirstReceive(testException);
        return testFirstReceive;
    }

}
