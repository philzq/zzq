package zzq.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 〈功能简述〉<br>
 * 〈用户角色中间表〉
 *
 * @author zhouzhiqiang
 * @create 2018-11-23
 */
@Data
@TableName("system_user_role")
public class UserRole extends SuperEntity{

    //用户id
    private String userId;
    //角色id
    private String roleId;
}