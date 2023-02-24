package zzq.zzqsimpleframeworkweb.aop;

import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint;
import org.springframework.stereotype.Component;
import zzq.zzqsimpleframeworkcommon.context.GlobalContext;
import zzq.zzqsimpleframeworkcommon.context.ProjectContext;
import zzq.zzqsimpleframeworkcommon.context.ThreadLocalManager;
import zzq.zzqsimpleframeworkcommon.entity.ProjectConstant;
import zzq.zzqsimpleframeworkjson.JacksonUtil;
import zzq.zzqsimpleframeworklog.LogUtilFactory;
import zzq.zzqsimpleframeworklog.entity.KafkaLogEntity;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 记录kafka消费日志并传递上下文信息
 *
 * @author zhouzhiqiang
 * @version 1.0
 * @date 2022-08-05 11:32
 */
@Aspect
@Component
public class KafkaListenerConsumerLogAspect {

    /**
     * 环绕通知(需要携带类型为ProceedingJoinPoint类型的参数)
     * 环绕通知包含前置、后置、返回、异常通知；ProceedingJoinPoin 类型的参数可以决定是否执行目标方法
     * 且环绕通知必须有返回值，返回值即目标方法的返回值
     *
     * @param point
     */
    @Around(value = "@annotation(org.springframework.kafka.annotation.KafkaListener)")
    public Object aroundMethod(ProceedingJoinPoint point) throws Throwable {
        boolean success = false;
        LocalDateTime startTime = LocalDateTime.now();
        Object result = null;
        Signature signature = ((MethodInvocationProceedingJoinPoint) point).getSignature();
        Throwable ex = null;
        KafkaLogEntity kafkaLogEntity = KafkaLogEntity.builder().build();
        ConsumerRecord consumerRecord = null;
        try {
            String className = signature.getDeclaringType().getName();
            String methodName = point.getSignature().getName();
            ProjectContext projectContext = ProjectContext.builder()
                    .className(className)
                    .methodName(methodName)
                    .build();
            //传递项目上下文信息
            ThreadLocalManager.setProjectContext(projectContext);

            //记录日志
            Object[] args = point.getArgs();
            for (int i = 0; i < args.length; i++) {
                Object arg = args[i];
                if (arg instanceof ConsumerRecord) {
                    consumerRecord = (ConsumerRecord) arg;
                }
            }
            if (consumerRecord != null) {
                //消费者日志记录
                kafkaLogEntity.setValue(consumerRecord.value());
                kafkaLogEntity.setTopic(consumerRecord.topic());
                kafkaLogEntity.setPartition(consumerRecord.partition());
                kafkaLogEntity.setKey(consumerRecord.key());
                kafkaLogEntity.setOperationType("Consumer");

                Map<String, String> headers = new HashMap<>();
                consumerRecord.headers().forEach(header -> {
                    String key = header.key();
                    byte[] value = header.value();
                    String strValue = new String(value, StandardCharsets.UTF_8);
                    headers.put(key, new String(value, StandardCharsets.UTF_8));

                    //如果存在上下文header，传递全局上下文信息
                    if (ProjectConstant.GLOBAL_CONTEXT_HEADER_KEY.equals(key)) {
                        ThreadLocalManager.setGlobalContext(JacksonUtil.parseJson(strValue, new TypeReference<GlobalContext>() {
                        }));
                    }
                });
                kafkaLogEntity.setHeaders(JacksonUtil.toJSon(headers));
            }
            String requestId = ThreadLocalManager.globalContextThreadLocal.get().getRequestId();
            kafkaLogEntity.setRequestId(requestId);

            result = point.proceed();
            success = true;
        } catch (Throwable e) {
            ex = e;
            LogUtilFactory.SYSTEM_ERROR.error("【kafka消费失败】", e.getMessage(), e);
            throw e;
        } finally {
            //清空上下文信息
            ThreadLocalManager.clear();

            //记录日志
            long elapseTime = Duration.between(startTime, LocalDateTime.now()).toMillis();
            kafkaLogEntity.setStartTime(startTime);
            kafkaLogEntity.setUri(signature.toLongString());
            kafkaLogEntity.setElapseTime(elapseTime);
            kafkaLogEntity.setSuccess(success);

            if (ex != null) {
                kafkaLogEntity.setStackTrace(ExceptionUtils.getStackTrace(ex));
            }

            LogUtilFactory.KAFKA.info(kafkaLogEntity);

        }
        return result;
    }
}
