package zzq.rpc;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import zzq.rpc.impl.UserServiceHystric;
import zzq.utils.R;

/**
 * 〈功能简述〉<br>
 * 〈远程调用用户信息〉
 *
 * @author zhouzhiqiang
 * @create 2018/11/17 0017
 */
@FeignClient(value = "admin",fallback = UserServiceHystric.class)
public interface UserService {

    @RequestMapping("admin/users")
    R findByUsername(@RequestParam("username") String username);
}
