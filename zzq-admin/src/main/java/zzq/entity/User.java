package zzq.entity;

import com.baomidou.mybatisplus.annotation.TableName;
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
public class User extends SuperEntity{

    //登录名
    @NotNull
    private String username;
    //密码
    @NotNull
    private String password;
    //用户名
    private String name;
    //生日
    private String birthday;
    //住址
    private String address;
    //手机电话
    private String mobilePhone;
    //座机电话
    private String telPhone;
    //邮箱
    private String email;
    //性别
    private String sex;
    //是否有效0有效1无效
    private String status;
    //描述
    private String description;
    //创建时间
    private Date createTime;
    //修改时间
    private Date updateTime;
}
