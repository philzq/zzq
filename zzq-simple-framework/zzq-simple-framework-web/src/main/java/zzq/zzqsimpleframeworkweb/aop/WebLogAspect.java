package zzq.zzqsimpleframeworkweb.aop;

import eu.bitwalker.useragentutils.UserAgent;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import zzq.zzqsimpleframeworkcommon.context.ProjectContext;
import zzq.zzqsimpleframeworkcommon.context.ThreadLocalManager;
import zzq.zzqsimpleframeworkjson.JacksonUtil;
import zzq.zzqsimpleframeworklog.LogUtilFactory;
import zzq.zzqsimpleframeworklog.entity.WebDigestLogEntity;
import zzq.zzqsimpleframeworkweb.servlet.CustomHttpServletRequestWrapper;

import javax.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.time.LocalDateTime;

/**
 * 方法执行性能跟踪日志切面
 * 支持注解和切点
 *
 * @author zhouzhiqiang
 * @version 1.0
 * @date 2022-08-05 11:32
 */
@Aspect
@Component
public class WebLogAspect {

    /**
     * 定义一个方法，用于声明切入点表达式，方法中一般不需要添加其他代码
     * 使用@Pointcut声明切入点表达式
     * 后面的通知直接使用方法名来引用当前的切点表达式；如果是其他类使用，加上包名即可
     */
    @Pointcut("execution(public * com.kdniao..controller..*.*(..))")
    public void methodPerformanceTracking() {
    }

    /**
     * 环绕通知(需要携带类型为ProceedingJoinPoint类型的参数)
     * 环绕通知包含前置、后置、返回、异常通知；ProceedingJoinPoin 类型的参数可以决定是否执行目标方法
     * 且环绕通知必须有返回值，返回值即目标方法的返回值
     *
     * @param point
     */
    @Around(value = "methodPerformanceTracking()")
    public Object aroundMethod(ProceedingJoinPoint point) throws Throwable {
        boolean success = false;
        LocalDateTime startTime = LocalDateTime.now();
        Object result = null;
        try {
            Signature signature = ((MethodInvocationProceedingJoinPoint) point).getSignature();
            String className = signature.getDeclaringType().getName();
            String methodName = point.getSignature().getName();
            ProjectContext projectContext = ProjectContext.builder()
                    .className(className)
                    .methodName(methodName)
                    .build();
            ThreadLocalManager.setProjectContext(projectContext);
            //初始化上下文
            result = point.proceed();
            success = true;
        } finally {
            //获取当前请求对象
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = attributes.getRequest();
            CustomHttpServletRequestWrapper customHttpServletRequestWrapper = new CustomHttpServletRequestWrapper(request);
            long elapseTime = Duration.between(startTime, LocalDateTime.now()).toMillis();
            String requestId = ThreadLocalManager.globalContextThreadLocal.get().getRequestId();
            UserAgent userAgent = customHttpServletRequestWrapper.getUserAgent();
            WebDigestLogEntity webDigestLogEntity = WebDigestLogEntity.builder()
                    .startTime(startTime)
                    .elapseTime(elapseTime)
                    .requestId(requestId)
                    .uri(customHttpServletRequestWrapper.getRequestURL().toString())
                    .domainName(customHttpServletRequestWrapper.getDomainName())
                    .remoteIp(customHttpServletRequestWrapper.getRemoteAddr())
                    .cookieId(customHttpServletRequestWrapper.getCookieId())
                    .sessionId(customHttpServletRequestWrapper.getSessionId(false))
                    .remoteOs(userAgent != null ? userAgent.getOperatingSystem().getName() : null)
                    .browserName(userAgent != null ? userAgent.getBrowser().getName() : null)
                    .request(customHttpServletRequestWrapper.getQueryString())
                    .response(JacksonUtil.toJSon(result))
                    .success(success)
                    .build();
            LogUtilFactory.WEB_DIGEST.info(webDigestLogEntity);
        }
        return result;
    }
}
