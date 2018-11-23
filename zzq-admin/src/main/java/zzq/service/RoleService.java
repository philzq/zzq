package zzq.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;
import zzq.entity.Role;

/**
 * 〈功能简述〉<br>
 * 〈角色〉
 *
 * @author zhouzhiqiang
 * @create 2018-11-23
 */
@Transactional(readOnly = true)
public interface RoleService extends IService<Role> {
}
