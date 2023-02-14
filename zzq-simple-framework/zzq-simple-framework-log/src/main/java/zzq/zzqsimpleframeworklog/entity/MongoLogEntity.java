package zzq.zzqsimpleframeworklog.entity;

import zzq.zzqsimpleframeworklog.entity.common.BaseGlobalLogEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author zhouzhiqiang
 * @version 1.0
 * @date 2023-02-09 19:15
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class MongoLogEntity extends BaseGlobalLogEntity {


    /**
     * 租户ID
     */
    private Integer tenantId;


    /**
     * 集合名称
     */
    private String collectionName;


    /**
     * 类方法
     */
    private String classMethod;


    /**
     * 错误信息 success=true时为空
     */
    private String errorMsg;
}
