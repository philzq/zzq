package zzq.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 〈功能简述〉<br>
 * 〈角色〉
 *
 * @author zhouzhiqiang
 * @create 2018-11-23
 */
@TableName("system_role")
@Data
public class Role extends SuperEntity{

    //角色名字
    private String name;
    //备注
    private String remark;
    //创建时间
    private Date createTime;
    //修改时间
    private Date updateTime;
}
