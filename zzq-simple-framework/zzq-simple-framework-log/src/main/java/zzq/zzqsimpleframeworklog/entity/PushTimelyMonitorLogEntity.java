package zzq.zzqsimpleframeworklog.entity;

import zzq.zzqsimpleframeworklog.entity.common.BaseGlobalLogEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author zhouzhiqiang
 * @version 1.0
 * @date 2023-02-09 19:38
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class PushTimelyMonitorLogEntity extends BaseGlobalLogEntity {

    private String requestType;
    private String partnerId;
    private String partnerGroupCode;
    private String pushurl;
    private boolean history;
    private long timeOffset;
    private String waybillCode;
    private String shipperCode;
    private String productTypeCode;
    private String notifyTypeCode;
    private String acceptStatusCode;
    private String baseAcceptStatusCode;
    private String serviceId;
    private boolean suspend;
}
