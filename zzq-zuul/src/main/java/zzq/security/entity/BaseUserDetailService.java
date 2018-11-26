package zzq.security.entity;

import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import zzq.rpc.UserService;
import zzq.utils.R;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 〈功能简述〉<br>
 * 〈自定义用户认证Service〉
 *
 * @author zhouzhiqiang
 * @create 2018-11-22
 */
public class BaseUserDetailService implements UserDetailsService {

    @Autowired
    UserService us;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        R r = us.findByUsername(username);
        Map<String,String> userMap = (Map)((List)r.get("user")).get(0);
        List<GrantedAuthority> authoritys = AuthorityUtils.createAuthorityList();
        authoritys.add(new SimpleGrantedAuthority("user"));
        return new User(userMap.get("username"),userMap.get("password"),authoritys);
    }
}
