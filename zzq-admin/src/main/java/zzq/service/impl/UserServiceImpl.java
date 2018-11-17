package zzq.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sun.org.apache.xerces.internal.util.EntityResolver2Wrapper;
import org.springframework.stereotype.Service;
import zzq.entity.User;
import zzq.mapper.UserMapper;
import zzq.service.UserService;
import zzq.utils.R;

/**
 * 〈功能简述〉<br>
 * 〈用户实现〉
 *
 * @author zhouzhiqiang
 * @create 2018/11/17 0017
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public R findOne(User user) {
        R r = new R();
        r.put("user",getOne(new QueryWrapper(user)));
        return r;
    }
}
