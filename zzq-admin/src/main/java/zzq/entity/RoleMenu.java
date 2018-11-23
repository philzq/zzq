package zzq.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 〈功能简述〉<br>
 * 〈角色菜单中间表〉
 *
 * @author zhouzhiqiang
 * @create 2018-11-23
 */
@Data
@TableName("system_role_menu")
public class RoleMenu extends SuperEntity{

    //角色ID
    private String roleId;
    //菜单id
    private String menuId;
}
