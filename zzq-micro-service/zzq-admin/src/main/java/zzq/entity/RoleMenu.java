package zzq.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 〈功能简述〉<br>
 * 〈角色菜单中间表〉
 *
 * @author zhouzhiqiang
 * @create 2018-11-23
 */
@Data
@TableName("system_role_menu")
@ApiModel(description = "角色菜单中间表")
public class RoleMenu extends SuperEntity{

    @NotNull
    @ApiModelProperty(value = "角色ID")
    private String roleId;

    @NotNull
    @ApiModelProperty(value = "菜单id")
    private String menuId;
}
