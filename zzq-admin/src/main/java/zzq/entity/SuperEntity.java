package zzq.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 演示实体父类
 */
@Data
public class SuperEntity<T extends Model> extends Model<T> {

    @ApiModelProperty(value = "主键id")
    private String id;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
