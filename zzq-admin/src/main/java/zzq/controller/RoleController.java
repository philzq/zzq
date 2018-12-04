package zzq.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
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
@Api(tags = "角色Controller")
public class RoleController {

    @PostMapping("role")
    @ApiOperation("新增角色")
    public R add(@Validated Role role) {
        role.setId(UUID.randomUUID().toString());
        role.setCreateTime(new Date());
        role.insert();
        return R.ok();
    }

    @DeleteMapping("role")
    @ApiOperation("删除角色")
    public R delete(Role role) {
        role.deleteById();
        return R.ok();
    }

    @PutMapping("role")
    @ApiOperation("修改角色")
    public R update(@Validated Role role){
        role.updateById();
        return R.ok();
    }
}
