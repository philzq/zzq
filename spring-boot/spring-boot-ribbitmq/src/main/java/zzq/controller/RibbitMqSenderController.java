package zzq.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zzq.production.Sender;

@RestController
@RequestMapping("sender")
public class RibbitMqSenderController {

    @Autowired
    private Sender sender;

    @GetMapping("testFirst")
    public String testFirst(String message) {
        sender.testFirst(message);
        return "testFirst";
    }


    @GetMapping("testReturnCallback")
    public String testReturnCallback(String message) {
        sender.testReturnCallback(message);
        return "testReturnCallback";
    }

    @GetMapping("testSecond")
    public String testSecond(String message) {
        sender.testSecond(message);
        return "testSecond";
    }
}
