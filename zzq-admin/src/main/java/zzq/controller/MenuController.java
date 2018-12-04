package zzq.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import zzq.entity.Menu;
import zzq.utils.R;

import java.util.Date;
import java.util.UUID;

/**
 * 〈功能简述〉<br>
 * 〈菜单〉
 *
 * @author zhouzhiqiang
 * @create 2018-11-25
 */
@RestController
@Api(tags = "菜单Controller")
public class MenuController {

    @PostMapping("menu")
    @ApiOperation(value = "新增菜单")
    public R add(@Validated Menu menu) {
        menu.setId(UUID.randomUUID().toString());
        menu.setCreateTime(new Date());
        menu.setEnabled("1");
        menu.insert();
        return R.ok();
    }

    @ApiOperation(value = "删除菜单")
    @DeleteMapping("menu")
    public R delete(Menu menu) {
        menu.deleteById();
        return R.ok();
    }

    @PutMapping("menu")
    @ApiOperation(value = "修改菜单")
    public R update(@Validated Menu menu){
        menu.updateById();
        return R.ok();
    }
}
