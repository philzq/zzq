package zzq.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zzq.entity.User;
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

    @RequestMapping("findByUsername")
    public R findByUsername(String username){
        return us.findByUsername(username);
    }
}
