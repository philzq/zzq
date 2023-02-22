package zzq.zzqsimpleframeworkweb.kafka;

import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import zzq.zzqsimpleframeworkcommon.context.GlobalContext;
import zzq.zzqsimpleframeworkcommon.context.ThreadLocalManager;
import zzq.zzqsimpleframeworkcommon.entity.ProjectConstant;
import zzq.zzqsimpleframeworkjson.JacksonUtil;
import zzq.zzqsimpleframeworklog.LogUtilFactory;
import zzq.zzqsimpleframeworklog.entity.KafkaLogEntity;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * kafka自定义消息生产拦截器
 *
 * @author zhouzhiqiang
 * @version 1.0
 * @date 2023-02-16 10:28
 */
public class KafkaProducerContextInterceptor implements ProducerInterceptor {
    @Override
    public ProducerRecord onSend(ProducerRecord record) {
        //传递上下文信息
        GlobalContext globalContext = ThreadLocalManager.globalContextThreadLocal.get();
        record.headers().add(ProjectConstant.GLOBAL_CONTEXT_HEADER_KEY, JacksonUtil.toJSon(globalContext).getBytes());

        //记录日志
        KafkaLogEntity kafkaLogEntity = KafkaLogEntity.builder()
                .value(record.value())
                .topic(record.topic())
                .partition(record.partition())
                .key(record.key())
                .operationType("Producer")
                .startTime(LocalDateTime.now())
                .success(true)
                .requestId(globalContext.getRequestId())
                .build();

        Map<String, String> headers = new HashMap<>();
        record.headers().forEach(header -> {
            String key = header.key();
            byte[] value = header.value();
            headers.put(key, new String(value, StandardCharsets.UTF_8));
        });
        kafkaLogEntity.setHeaders(JacksonUtil.toJSon(headers));

        LogUtilFactory.KAFKA.info(kafkaLogEntity);
        return record;
    }

    @Override
    public void onAcknowledgement(RecordMetadata metadata, Exception exception) {

    }

    @Override
    public void close() {

    }

    @Override
    public void configure(Map<String, ?> configs) {

    }
}
