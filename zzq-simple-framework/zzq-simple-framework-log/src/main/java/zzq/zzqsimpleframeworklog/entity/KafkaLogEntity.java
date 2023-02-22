package zzq.zzqsimpleframeworklog.entity;

import zzq.zzqsimpleframeworklog.entity.common.BaseGlobalLogEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author zhouzhiqiang
 * @version 1.0
 * @date 2023-02-21 13:46
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class KafkaLogEntity extends BaseGlobalLogEntity {

    /**
     * kafka 主题
     */
    private String topic;

    /**
     * 分区
     */
    private Integer partition;

    /**
     * 请求头
     */
    private String headers;

    /**
     * 分区key
     */
    private Object key;

    /**
     * 消息值
     */
    private Object value;

    /**
     * Producer 生产者，Consumer 消费者
     */
    private String operationType;
}
