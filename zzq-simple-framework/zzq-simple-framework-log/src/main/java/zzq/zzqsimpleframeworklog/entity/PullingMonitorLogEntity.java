package zzq.zzqsimpleframeworklog.entity;

import zzq.zzqsimpleframeworklog.entity.common.BaseGlobalLogEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author zhouzhiqiang
 * @version 1.0
 * @date 2023-02-09 19:37
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class PullingMonitorLogEntity extends BaseGlobalLogEntity {

    private String partnerId;
    private String customerName;
    private String partnerGroup;
    private String shipperCode;
    private String waybillCode;
    private String channel;
    private String interfaceTypeCode;
    private boolean reject = true;
    private boolean async;
    private String resultCode;
    private String resultDesc;
}
