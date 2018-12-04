package zzq.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 〈功能简述〉<br>
 * 〈用户实体类〉
 *
 * @author zhouzhiqiang
 * @create 2018/11/17 0017
 */
@TableName("system_user")
@Data
@ApiModel(value = "用户实体类")
public class User extends SuperEntity{

    @NotNull
    @ApiModelProperty(value = "登录名")
    private String username;

    @NotNull
    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "用户名")
    private String name;

    @ApiModelProperty(value = "生日")
    private String birthday;

    @ApiModelProperty(value = "住址")
    private String address;

    @ApiModelProperty(value = "手机电话")
    private String mobilePhone;

    @ApiModelProperty(value = "座机电话")
    private String telPhone;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "性别")
    private String sex;

    @ApiModelProperty(value = "是否有效0无效1有效")
    private String status;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    private Date updateTime;
}
