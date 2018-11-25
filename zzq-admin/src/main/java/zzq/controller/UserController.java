package zzq.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zzq.entity.User;
import zzq.service.UserService;
import zzq.utils.R;

import java.util.Date;
import java.util.UUID;

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

    @RequestMapping("findByUsername")
    public R findByUsername(String username) {
        R r = new R();
        r.put("user", us.getOne(new QueryWrapper<User>().eq("username", username)));
        return r;
    }

    @RequestMapping("add")
    public R add(@Validated User user) {
        user.setId(UUID.randomUUID().toString());
        user.setStatus("0");
        user.setCreateTime(new Date());
        user.setPassword("{bcrypt}"+new BCryptPasswordEncoder().encode(user.getPassword()));
        user.insert();
        return R.ok();
    }

    @RequestMapping("delete")
    public R delete(User user) {
        user.deleteById();
        return R.ok();
    }

    @RequestMapping("update")
    public R update(User user){
        user.updateById();
        return R.ok();
    }


}
