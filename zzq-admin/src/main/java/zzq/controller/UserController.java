package zzq.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.ibatis.annotations.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
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
public class UserController {

    @Autowired
    UserService us;

    @GetMapping("users")
    public R findAll(User user) {
        R r = new R();
        r.put("user", user.selectList(new QueryWrapper(user)));
        return r;
    }

    @GetMapping("users/{username}")
    public R findByUsername(@PathVariable("username") String username){
        R r = new R();
        r.put("user", us.getOne(new QueryWrapper<>(new User()).eq("username",username)));
        return r;
    }

    @PostMapping("user")
    public R add(@Validated User user) {
        user.setId(UUID.randomUUID().toString());
        user.setStatus("0");
        user.setCreateTime(new Date());
        user.setPassword("{bcrypt}"+new BCryptPasswordEncoder().encode(user.getPassword()));
        user.insert();
        return R.ok();
    }

    @DeleteMapping("user")
    public R delete(User user) {
        user.deleteById();
        return R.ok();
    }

    @PutMapping("user")
    public R update(User user){
        user.updateById();
        return R.ok();
    }


}
