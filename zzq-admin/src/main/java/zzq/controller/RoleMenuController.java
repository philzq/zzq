package zzq.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import zzq.entity.RoleMenu;
import zzq.utils.R;

import java.util.UUID;

/**
 * 〈功能简述〉<br>
 * 〈角色菜单〉
 *
 * @author zhouzhiqiang
 * @create 2018-11-25
 */
@RestController
public class RoleMenuController {

    @PostMapping("rolemenu")
    public R add(@Validated RoleMenu roleMenu) {
        roleMenu.setId(UUID.randomUUID().toString());
        roleMenu.insert();
        return R.ok();
    }

    @DeleteMapping("rolemenu")
    public R delete(RoleMenu roleMenu) {
        roleMenu.deleteById();
        return R.ok();
    }

    @PutMapping("rolemenu")
    public R update(@Validated RoleMenu roleMenu){
        roleMenu.updateById();
        return R.ok();
    }
}
