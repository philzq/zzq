package zzq.rpc.impl;

import org.springframework.stereotype.Component;
import zzq.rpc.UserService;
import zzq.utils.R;

/**
 * 〈功能简述〉<br>
 * 〈用户rpc断路器〉
 *
 * @author zhouzhiqiang
 * @create 2018-11-23
 */
@Component
public class UserServiceHystric implements UserService {

    @Override
    public R findByUsername(String username) {
        return R.error(username);
    }
}
