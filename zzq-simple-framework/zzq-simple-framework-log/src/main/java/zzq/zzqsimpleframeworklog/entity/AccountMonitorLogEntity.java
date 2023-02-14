package zzq.zzqsimpleframeworklog.entity;

import zzq.zzqsimpleframeworklog.entity.common.BaseGlobalLogEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author zhouzhiqiang
 * @version 1.0
 * @date 2023-02-09 19:16
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class AccountMonitorLogEntity extends BaseGlobalLogEntity {

    private String requestTypeCode;
    private String productTypeCode;
    private String partnerId;
    private String partnerGroupCode;
    private String waybillCode;
    private String shipperCode;
    private String acceptStatusCode;
    private String baseAcceptStatusCode;
    // 当次消费是否收费
    private boolean charge;
    private String resultCode;
    private String resultDesc;
    private String serviceId;
    private String orderId;
    private String phoneNo;
    private String remoteIp;
    private String callback;
}
