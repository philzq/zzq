package zzq.zzqsimpleframeworklog.entity;

import zzq.zzqsimpleframeworklog.entity.common.BaseGlobalLogEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author zhouzhiqiang
 * @version 1.0
 * @date 2023-02-09 19:14
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class RpcDigestLogEntity extends BaseGlobalLogEntity {

    private String remoteAppName;
    private String errorCode;
    private String errorDesc;
    private String remoteIp;
}
