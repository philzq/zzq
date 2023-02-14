package zzq.zzqsimpleframeworklog.entity;

import zzq.zzqsimpleframeworklog.entity.common.BaseGlobalLogEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * web.digest日志实体
 *
 * @author zhouzhiqiang
 * @version 1.0
 * @date 2023-02-09 17:33
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class WebDigestLogEntity extends BaseGlobalLogEntity {

    private String errorCode;
    private String errorDesc;
    private String domainName;
    private String remoteIp;
    private String remoteOs;
    private String browserName;
    private String cookieId;
    private String sessionId;
}
