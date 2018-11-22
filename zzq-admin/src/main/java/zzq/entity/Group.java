package zzq.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 〈功能简述〉<br>
 * 〈组〉
 *
 * @author zhouzhiqiang
 * @create 2018/11/17 0017
 */
@TableName("zzq_group")
@Data
public class Group extends SuperEntity{

    //名字
    private String name;
    //父级
    private String parentId;
    //类型1部门2角色
    private String type;
    //描述
    private String description;
}
