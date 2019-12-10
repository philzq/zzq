package zzq.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 〈功能简述〉<br>
 * 〈用户角色中间表〉
 *
 * @author zhouzhiqiang
 * @create 2018-11-23
 */
@Data
@TableName("system_user_role")
@ApiModel(description = "用户角色中间表")
public class UserRole extends SuperEntity{

    @NotNull
    @ApiModelProperty(value = "用户id")
    private String userId;

    @NotNull
    @ApiModelProperty(value = "角色id")
    private String roleId;
}
