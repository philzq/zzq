package zzq.zzqsimpleframeworklog.entity;

import zzq.zzqsimpleframeworklog.entity.common.BaseGlobalLogEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
public class RemoteDigestLogEntity extends BaseGlobalLogEntity {

        private String remoteAppName;
        private int errorCode;
        private String errorDesc;
        private String remoteIp;
        @Builder.Default
        private StringBuffer requestDetail = new StringBuffer();

}
