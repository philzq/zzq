package zzq.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@Api(tags = "角色菜单中间表")
public class RoleMenuController {

    @PostMapping("rolemenu")
    @ApiOperation(value = "新增")
    public R add(@Validated RoleMenu roleMenu) {
        roleMenu.setId(UUID.randomUUID().toString());
        roleMenu.insert();
        return R.ok();
    }

    @DeleteMapping("rolemenu")
    @ApiOperation(value = "删除")
    public R delete(RoleMenu roleMenu) {
        roleMenu.deleteById();
        return R.ok();
    }

    @PutMapping("rolemenu")
    @ApiOperation(value = "修改")
    public R update(@Validated RoleMenu roleMenu){
        roleMenu.updateById();
        return R.ok();
    }
}
