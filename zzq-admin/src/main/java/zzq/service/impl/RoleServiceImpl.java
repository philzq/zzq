package zzq.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import zzq.entity.Role;
import zzq.mapper.RoleMapper;
import zzq.service.RoleService;

/**
 * 〈功能简述〉<br>
 * 〈角色〉
 *
 * @author zhouzhiqiang
 * @create 2018-11-23
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {
}
