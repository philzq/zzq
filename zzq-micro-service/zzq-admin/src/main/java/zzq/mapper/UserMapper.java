package zzq.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import zzq.entity.User;

import java.util.List;
import java.util.Map;

/**
 * 〈功能简述〉<br>
 * 〈用户mapper〉
 *
 * @author zhouzhiqiang
 * @create 2018/11/17 0017
 */
public interface UserMapper extends SuperMapper<User>{

    /**
     * 分页
     * @param page
     * @param user
     * @return
     */
    List<Map<String,Object>> selectUserPage(Page page,User user);
}
