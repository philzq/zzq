package zzq.zzqsimpleframeworktlogclient.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import zzq.zzqsimpleframeworklog.entity.common.BaseLogEntity;

/**
 * @author zhouzhiqiang
 * @version 1.0
 * @date 2023-02-14 10:49
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class TestLog extends BaseLogEntity {

    private String test;
}
