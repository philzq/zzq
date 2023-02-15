package zzq.zzqsimpleframeworklog.entity;

import zzq.zzqsimpleframeworklog.entity.common.BaseGlobalLogEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 任务日志
 *
 * @author zhouzhiqiang
 * @version 1.0
 * @date 2023-02-15 16:11
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class TaskLogEntity extends BaseGlobalLogEntity {

    private String taskName;
}
