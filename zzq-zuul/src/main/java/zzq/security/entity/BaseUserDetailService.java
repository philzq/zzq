package zzq.security.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import zzq.rpc.UserService;

/**
 * 〈功能简述〉<br>
 * 〈自定义用户认证Service〉
 *
 * @author zhouzhiqiang
 * @create 2018-11-22
 */
@Service
public class BaseUserDetailService implements UserDetailsService {

    @Autowired
    UserService us;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {


        return null;
    }
}
