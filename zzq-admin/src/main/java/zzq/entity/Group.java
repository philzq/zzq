package zzq.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;

/**
 * 〈功能简述〉<br>
 * 〈组〉
 *
 * @author zhouzhiqiang
 * @create 2018/11/17 0017
 */
@TableName("zzq_group")
public class Group extends SuperEntity{

    //名字
    private String name;
    //父级
    private String parentId;
    //路径
    private String path;
    //类型
    private String type;
    //组类型
    private String groupType;
    //描述
    private String description;
}
