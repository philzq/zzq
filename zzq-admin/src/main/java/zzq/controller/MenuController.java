package zzq.controller;

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
public class MenuController {

    @PostMapping("menu")
    public R add(@Validated Menu menu) {
        menu.setId(UUID.randomUUID().toString());
        menu.setCreateTime(new Date());
        menu.setEnabled("0");
        menu.insert();
        return R.ok();
    }

    @DeleteMapping("menu")
    public R delete(Menu menu) {
        menu.deleteById();
        return R.ok();
    }

    @PutMapping("menu")
    public R update(@Validated Menu menu){
        menu.updateById();
        return R.ok();
    }
}
