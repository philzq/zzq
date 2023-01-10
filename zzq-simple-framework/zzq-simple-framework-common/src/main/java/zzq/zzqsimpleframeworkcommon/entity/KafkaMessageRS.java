package zzq.zzqsimpleframeworkcommon.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

/**
 * kafka结果集封装
 *
 * @author zhouzhiqiang
 * @version 1.0
 * @date 2022-04-24 12:01
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class KafkaMessageRS<T> {

    @Builder.Default
    private String id = UUID.randomUUID().toString();

    /**
     * 消息内容
     */
    private T data;


    /**
     * 获取kafka消息实体
     *
     * @param data
     * @param <T>
     * @return
     */
    public static <T> KafkaMessageRS<T> getInstall(T data) {
        KafkaMessageRS<T> kafkaMessageRS = new KafkaMessageRS<>();
        kafkaMessageRS.setData(data);
        return kafkaMessageRS;
    }


}
