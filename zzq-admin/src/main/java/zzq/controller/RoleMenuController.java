package zzq.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
@RequestMapping("rolemenu")
public class RoleMenuController {

    @RequestMapping("add")
    public R add(@Validated RoleMenu roleMenu) {
        roleMenu.setId(UUID.randomUUID().toString());
        roleMenu.insert();
        return R.ok();
    }

    @RequestMapping("delete")
    public R delete(RoleMenu roleMenu) {
        roleMenu.deleteById();
        return R.ok();
    }

    @RequestMapping("update")
    public R update(@Validated RoleMenu roleMenu){
        roleMenu.updateById();
        return R.ok();
    }
}