package zzq.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
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

    @RequestMapping("findByUsername")
    public R findByUsername(String username){
        R r = new R();
        try {
            r.put("user",us.getOne(new QueryWrapper<User>().eq("username",username)));
            return r;
        }catch (Exception e){
            e.printStackTrace();
            return R.error();
        }
    }

    @RequestMapping("add")
    public R insertOrUpdate(User user){
        try {
            user.insertOrUpdate();
            return R.ok();
        }catch (Exception e){
            e.printStackTrace();
            return R.error();
        }
    }

    @RequestMapping("delete")
    public R delete(User user){
        try {
            user.deleteById();
            return R.ok();
        }catch (Exception e){
            e.printStackTrace();
            return R.error();
        }
    }
}
