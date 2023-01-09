package zzq.zzqsimpleframeworkcommon.entity;

import lombok.AllArgsConstructor;
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

    private KafkaMessageHead head;

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
        KafkaMessageHead kafkaMessageHead = new KafkaMessageHead();
        KafkaMessageRS<T> kafkaMessageRS = new KafkaMessageRS<>();
        kafkaMessageRS.setHead(kafkaMessageHead);
        kafkaMessageRS.setData(data);
        return kafkaMessageRS;
    }

    @Data
    public static class KafkaMessageHead {
        private String id = UUID.randomUUID().toString();

    }

}
