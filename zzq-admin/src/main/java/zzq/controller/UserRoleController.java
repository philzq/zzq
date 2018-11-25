package zzq.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zzq.entity.UserRole;
import zzq.utils.R;

import java.util.UUID;

/**
 * 〈功能简述〉<br>
 * 〈用户角色〉
 *
 * @author zhouzhiqiang
 * @create 2018-11-25
 */
@RestController
@RequestMapping("userrole")
public class UserRoleController {

    @RequestMapping("add")
    public R add(@Validated UserRole userRole) {
        userRole.setId(UUID.randomUUID().toString());
        userRole.insert();
        return R.ok();
    }

    @RequestMapping("delete")
    public R delete(UserRole userRole) {
        userRole.deleteById();
        return R.ok();
    }

    @RequestMapping("update")
    public R update(@Validated UserRole userRole){
        userRole.updateById();
        return R.ok();
    }
}
