package zzq.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zzq.entity.Role;
import zzq.utils.R;

import java.util.Date;
import java.util.UUID;

/**
 * 〈功能简述〉<br>
 * 〈角色〉
 *
 * @author zhouzhiqiang
 * @create 2018-11-25
 */
@RestController
@RequestMapping("role")
public class RoleController {

    @RequestMapping("add")
    public R add(@Validated Role role) {
        role.setId(UUID.randomUUID().toString());
        role.setCreateTime(new Date());
        role.insert();
        return R.ok();
    }

    @RequestMapping("delete")
    public R delete(Role role) {
        role.deleteById();
        return R.ok();
    }

    @RequestMapping("update")
    public R update(@Validated Role role){
        role.updateById();
        return R.ok();
    }
}
