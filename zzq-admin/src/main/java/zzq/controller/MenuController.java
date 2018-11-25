package zzq.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
@RequestMapping("menu")
public class MenuController {

    @RequestMapping("add")
    public R add(@Validated Menu menu) {
        menu.setId(UUID.randomUUID().toString());
        menu.setCreateTime(new Date());
        menu.setEnabled("0");
        menu.insert();
        return R.ok();
    }

    @RequestMapping("delete")
    public R delete(Menu menu) {
        menu.deleteById();
        return R.ok();
    }

    @RequestMapping("update")
    public R update(@Validated Menu menu){
        menu.updateById();
        return R.ok();
    }
}
