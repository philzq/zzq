package zzq.controller;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.segments.MergeSegments;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zzq.entity.User;
import zzq.mapper.UserMapper;

/**
 * 〈功能简述〉<br>
 * 〈用户controller〉
 *
 * @author zhouzhiqiang
 * @create 2018/11/17 0017
 */
@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    UserMapper um;

    @RequestMapping("findByPage")
    public Object findByPage(Page page, User user) {
        return page.setRecords(um.selectUserPage(page,user));
    }
}
