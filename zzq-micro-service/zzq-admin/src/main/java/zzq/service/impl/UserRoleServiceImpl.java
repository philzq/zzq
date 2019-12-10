package zzq.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import zzq.entity.UserRole;
import zzq.mapper.UserRoleMapper;
import zzq.service.UserRoleService;

/**
 * 〈功能简述〉<br>
 * 〈用户角色〉
 *
 * @author zhouzhiqiang
 * @create 2018-11-23
 */
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {
}
