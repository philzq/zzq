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
public class PushMonitorLogEntity extends BaseGlobalLogEntity {

    private String requestType;
    private String partnerId;
    private String resultCode;
    private String resultDesc;
    private long sysDelayTime;
    private int retriedCount;
    private String notifyChannel;
}
