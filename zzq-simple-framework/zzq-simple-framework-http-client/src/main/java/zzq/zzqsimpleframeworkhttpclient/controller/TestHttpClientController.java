package zzq.zzqsimpleframeworkhttpclient.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zzq.zzqsimpleframeworkhttpclient.test.TestHttpClientService;

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

    @GetMapping("getOK")
    public String getOK(){
        String ok = testHttpClientService.getOK();
        return ok;
    }
}
