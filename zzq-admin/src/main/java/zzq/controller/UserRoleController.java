package zzq.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
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
@Api(tags = "用户角色Controller")
public class UserRoleController {

    @PostMapping("userrole")
    @ApiOperation(value = "新增")
    public R add(@Validated UserRole userRole) {
        userRole.setId(UUID.randomUUID().toString());
        userRole.insert();
        return R.ok();
    }

    @DeleteMapping("userrole")
    @ApiOperation(value = "删除")
    public R delete(UserRole userRole) {
        userRole.deleteById();
        return R.ok();
    }

    @PutMapping("userrole")
    @ApiOperation(value = "修改")
    public R update(@Validated UserRole userRole){
        userRole.updateById();
        return R.ok();
    }
}
