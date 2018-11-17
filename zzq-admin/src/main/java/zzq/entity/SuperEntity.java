package zzq.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

/**
 * 演示实体父类
 */
@Data
public class SuperEntity<T extends Model> extends Model<T> {

    //主键id
    private String id;
    //创建时间
    private Date crtTime;
    //创建人id
    private String crtUser;
    //创建人名字
    private String crtName;
    //创建人ip
    private String crtHost;
    //修改时间
    private Date updTime;
    //修改人id
    private String updUser;
    //修改人名字
    private String updName;
    //修改人ip
    private String updHost;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
