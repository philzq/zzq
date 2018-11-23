package zzq.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import zzq.entity.Menu;
import zzq.mapper.MenuMapper;
import zzq.service.MenuService;

/**
 * 〈功能简述〉<br>
 * 〈菜单〉
 *
 * @author zhouzhiqiang
 * @create 2018-11-23
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {
}
