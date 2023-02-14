package zzq.zzqsimpleframeworklog.entity;

import zzq.zzqsimpleframeworklog.entity.common.BaseGlobalLogEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author zhouzhiqiang
 * @version 1.0
 * @date 2023-02-09 19:16
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class KafkaConsumerLogEntity extends BaseGlobalLogEntity {


    /**
     * 消息ID
     */
    private String messageId;

    /**
     * 租户ID
     */
    private Integer tenantId;

    /**
     * 批次ID 可以用于统计某一批次订阅耗时 可为空
     */
    private String batchId;

    /**
     * topic名称
     */
    private String topic;

    /**
     * groupId
     */
    private String groupId;

    /**
     * 分区
     */
    private int partition;

    /**
     * key
     */
    private Object key;

    /**
     * 消息内容
     */
    private Object message;

    /**
     * 消息内存占用 单位字节
     */
    private long messageSize;


    /**
     * 失败信息
     */
    private String errorMsg;

    /**
     * 物流单号
     */
    private String waybillCode;

    /**
     * 快递公司编码
     */
    private String shipperCode;
}
