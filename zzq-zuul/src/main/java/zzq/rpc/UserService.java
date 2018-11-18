package zzq.rpc;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import zzq.entity.User;
import zzq.utils.R;

/**
 * 〈功能简述〉<br>
 * 〈远程调用用户信息〉
 *
 * @author zhouzhiqiang
 * @create 2018/11/17 0017
 */
@FeignClient("admin")
@RequestMapping("admin/user")
public interface UserService {

    @RequestMapping("findOne")
    R findOne(String username);
}
