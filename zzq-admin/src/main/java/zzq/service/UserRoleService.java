package zzq.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;
import zzq.entity.UserRole;

/**
 * 〈功能简述〉<br>
 * 〈用户角色〉
 *
 * @author zhouzhiqiang
 * @create 2018-11-23
 */
@Transactional(readOnly = true)
public interface UserRoleService extends IService<UserRole> {
}
