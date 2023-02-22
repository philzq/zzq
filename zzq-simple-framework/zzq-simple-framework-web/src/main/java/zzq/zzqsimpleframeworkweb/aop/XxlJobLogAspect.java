package zzq.zzqsimpleframeworkweb.aop;

import com.xxl.job.core.context.XxlJobContext;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint;
import org.springframework.stereotype.Component;
import zzq.zzqsimpleframeworkcommon.context.ProjectContext;
import zzq.zzqsimpleframeworkcommon.context.ThreadLocalManager;
import zzq.zzqsimpleframeworklog.LogUtilFactory;
import zzq.zzqsimpleframeworklog.entity.TaskLogEntity;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * 记录xxljob日志并传递上下文信息
 *
 * @author zhouzhiqiang
 * @version 1.0
 * @date 2022-08-05 11:32
 */
@Aspect
@Component
public class XxlJobLogAspect {

    /**
     * 环绕通知(需要携带类型为ProceedingJoinPoint类型的参数)
     * 环绕通知包含前置、后置、返回、异常通知；ProceedingJoinPoin 类型的参数可以决定是否执行目标方法
     * 且环绕通知必须有返回值，返回值即目标方法的返回值
     *
     * @param point
     */
    @Around(value = "@annotation(com.xxl.job.core.handler.annotation.XxlJob)")
    public Object aroundMethod(ProceedingJoinPoint point) throws Throwable {
        boolean success = false;
        LocalDateTime startTime = LocalDateTime.now();
        Object result = null;
        Signature signature = ((MethodInvocationProceedingJoinPoint) point).getSignature();
        Throwable ex = null;
        String requestId = null;
        try {
            String className = signature.getDeclaringType().getName();
            String methodName = point.getSignature().getName();
            ProjectContext projectContext = ProjectContext.builder()
                    .className(className)
                    .methodName(methodName)
                    .build();
            //传递项目上下文信息
            ThreadLocalManager.setProjectContext(projectContext);
            //初始化全局上下文信息
            requestId = ThreadLocalManager.globalContextThreadLocal.get().getRequestId();

            result = point.proceed();
            success = true;
        } catch (Throwable e) {
            ex = e;
            LogUtilFactory.SYSTEM_ERROR.error("【xxljob执行失败】", e.getMessage(), e);
            throw e;
        } finally {
            long elapseTime = Duration.between(startTime, LocalDateTime.now()).toMillis();
            //清空上下文信息
            ThreadLocalManager.clear();
            //记录日志
            XxlJobContext xxlJobContext = XxlJobContext.getXxlJobContext();
            TaskLogEntity taskLogEntity = TaskLogEntity.builder()
                    .shardTotal(xxlJobContext.getShardTotal())
                    .shardIndex(xxlJobContext.getShardIndex())
                    .jobId(xxlJobContext.getJobId())
                    .startTime(startTime)
                    .elapseTime(elapseTime)
                    .requestId(requestId)
                    .success(success)
                    .uri(signature.toLongString())
                    .request(xxlJobContext.getJobParam())
                    .response(xxlJobContext.getHandleMsg())
                    .build();
            if (ex != null) {
                taskLogEntity.setStackTrace(ExceptionUtils.getStackTrace(ex));
            }
            LogUtilFactory.TASK.info(taskLogEntity);
        }
        return result;
    }
}
