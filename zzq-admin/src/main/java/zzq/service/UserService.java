package zzq.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;
import zzq.entity.User;
import zzq.utils.R;

/**
 * 〈功能简述〉<br>
 * 〈用户〉
 *
 * @author zhouzhiqiang
 * @create 2018/11/17 0017
 */
@Transactional(readOnly = true)
public interface UserService extends IService<User> {

    /**
     * 查询所有的用户
     * @param user
     * @return
     */
    R findOne(User user);

}
