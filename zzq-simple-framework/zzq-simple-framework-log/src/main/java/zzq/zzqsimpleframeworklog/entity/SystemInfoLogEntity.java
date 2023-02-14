package zzq.zzqsimpleframeworklog.entity;

import zzq.zzqsimpleframeworklog.entity.common.BaseLogEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author zhouzhiqiang
 * @version 1.0
 * @date 2023-02-09 10:33
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class SystemInfoLogEntity extends BaseLogEntity {

    /**
     * 标题
     */
    private String title;

    /**
     * 消息体
     */
    private String message;

}
