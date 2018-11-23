package zzq.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 〈功能简述〉<br>
 * 〈菜单〉
 *
 * @author zhouzhiqiang
 * @create 2018/11/17 0017
 */
@TableName("system_menu")
@Data
public class Menu extends SuperEntity{

    //标题
    private String title;
    //父级节点
    private String parentId;
    //权限标识
    private String permission;
    //资源路径
    private String href;
    //图标
    private String icon;
    //排序
    private Integer orderNum;
    //描述
    private String description;
    //启用禁用
    private String enabled;
    //是否显示
    private String showed;
    //创建时间
    private Date createTime;
    //修改时间
    private Date updateTime;
}
