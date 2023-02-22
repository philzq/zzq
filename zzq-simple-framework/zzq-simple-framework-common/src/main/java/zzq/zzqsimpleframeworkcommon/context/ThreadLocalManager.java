package zzq.zzqsimpleframeworkcommon.context;

import com.alibaba.ttl.TransmittableThreadLocal;

import java.util.UUID;

/**
 * 线程副本管理
 * <p>
 * 一个aop      新增、删除上下文
 * 一个kafka    新增、删除上下文
 * 一个job      新增、删除上下文
 * 一个统一异常处理    删除上下文
 * <p>
 * 上下文链路传输
 *
 * @author zhouzhiqiang
 * @version 1.0
 * @date 2023-02-15 10:43
 */
public class ThreadLocalManager {

    /**
     * 存储全局上下文信息的本地副本
     */
    public static final TransmittableThreadLocal<GlobalContext> globalContextThreadLocal = new TransmittableThreadLocal<>() {
        @Override
        protected GlobalContext initialValue() {
            return GlobalContext.builder()
                    .requestId(UUID.randomUUID().toString())
                    .build();
        }
    };

    /**
     * 存储全局上下文信息的本地副本
     */
    public static final TransmittableThreadLocal<ProjectContext> projectContextThreadLocal = new TransmittableThreadLocal<>() {
        @Override
        protected ProjectContext initialValue() {
            return ProjectContext.builder().build();
        }
    };

    /**
     * 设置全链路上下文信息  --- 增量设置，有什么值设置什么值
     *
     * @param globalContext
     */
    public static void setGlobalContext(GlobalContext globalContext) {
        if (globalContext == null) {
            return;
        }
        if (globalContext.getRequestId() != null) {
            globalContextThreadLocal.get().setRequestId(globalContext.getRequestId());
        }
    }

    /**
     * 设置项目上下文  --- 增量设置，有什么值设置什么值
     * @param projectContext
     */
    public static void setProjectContext(ProjectContext projectContext){
        if(projectContext == null){
            return;
        }
        if(projectContext.getClassName() != null){
            projectContextThreadLocal.get().setClassName(projectContext.getClassName());
        }
        if(projectContext.getMethodName() != null){
            projectContextThreadLocal.get().setMethodName(projectContext.getMethodName());
        }
    }

    /**
     * 清除线程副本
     */
    public static void clear() {
        globalContextThreadLocal.remove();
        projectContextThreadLocal.remove();
    }
}
