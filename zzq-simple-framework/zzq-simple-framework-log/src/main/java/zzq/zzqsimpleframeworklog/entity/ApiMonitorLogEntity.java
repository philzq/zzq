package zzq.zzqsimpleframeworklog.entity;

import zzq.zzqsimpleframeworklog.entity.common.BaseGlobalLogEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author zhouzhiqiang
 * @version 1.0
 * @date 2023-02-09 19:36
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class ApiMonitorLogEntity extends BaseGlobalLogEntity {

    private String partnerId;
    private String requestType;

    /**
     * 产品类型
     */
    private String productType;

    /**
     * 渠道
     */
    private String shipperCode;
}
