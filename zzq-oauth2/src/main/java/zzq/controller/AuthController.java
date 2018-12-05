package zzq.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * 〈功能简述〉<br>
 * 〈认证控制中心〉
 *
 * @author zhouzhiqiang
 * @create 2018-12-05
 */
@RestController
public class AuthController {

    @GetMapping("user")
    public Principal getUser(Principal principal){
        return principal;
    }
}
