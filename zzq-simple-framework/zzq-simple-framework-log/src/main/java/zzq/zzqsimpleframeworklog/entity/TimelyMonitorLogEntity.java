package zzq.zzqsimpleframeworklog.entity;

import zzq.zzqsimpleframeworklog.entity.common.BaseGlobalLogEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author zhouzhiqiang
 * @version 1.0
 * @date 2023-02-09 19:39
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class TimelyMonitorLogEntity extends BaseGlobalLogEntity {

    private String partnerId;
    private String partnerGroup;
    private String waybillCode;
    private String shipperCode;
    private String triggerType;
    private String canal;
    private long delayTime;
    private long sysDelayTime;
    private boolean history;
    private long subscribeTime;
    private long preExcuteTime;
    private long excuteTimes;
    private long lastTraceTime;
    private String acceptStatus;

}
