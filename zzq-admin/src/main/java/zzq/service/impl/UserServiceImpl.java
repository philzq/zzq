package zzq.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import zzq.entity.User;
import zzq.mapper.UserMapper;
import zzq.service.UserService;

/**
 * 〈功能简述〉<br>
 * 〈用户实现〉
 *
 * @author zhouzhiqiang
 * @create 2018/11/17 0017
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
