package zzq.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zzq.rpc.UserService;
import zzq.utils.R;

/**
 * 〈功能简述〉<br>
 * 〈用户控制中心〉
 *
 * @author zhouzhiqiang
 * @create 2018-11-18
 */
@RestController
public class AdminController {

    @Autowired
    private UserService us;

    @RequestMapping("findOne")
    public R findOne(String username){
        return us.findOne(username);
    }
}
