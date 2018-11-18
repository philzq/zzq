package zzq.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zzq.entity.User;
import zzq.service.UserService;
import zzq.utils.R;

/**
 * 〈功能简述〉<br>
 * 〈用户controller〉
 *
 * @author zhouzhiqiang
 * @create 2018/11/17 0017
 */
@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    UserService us;

    @RequestMapping("findOne")
    public R findOne(User user) {
        R r = null;
        try {
            r = us.findOne(user);
        }catch (Exception e){
            e.printStackTrace();
            return R.error("操作失败");
        }
        return r;
    }
}
