package zzq.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
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
@ApiModel(description = "菜单实体类")
public class Menu extends SuperEntity{

    @NotNull
    @ApiModelProperty("标题")
    private String title;

    @NotNull
    @ApiModelProperty("父级节点")
    private String parentId;

    @ApiModelProperty("权限标识")
    private String permission;

    @NotNull
    @ApiModelProperty("资源路径")
    private String href;

    @ApiModelProperty("图标")
    private String icon;

    @NotNull
    @ApiModelProperty("排序")
    private Integer orderNum;

    @NotNull
    @ApiModelProperty("描述")
    private String description;

    @ApiModelProperty("启用禁用0禁用1启用")
    private String enabled;

    @NotNull
    @ApiModelProperty("是否显示0不显示1显示")
    private String showed;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("修改时间")
    private Date updateTime;
}
