package zzq.zzqsimpleframeworkcommon.util;

import java.util.concurrent.*;

/**
 * 自定义线程池  --  简化线程池使用
 * 核心参数尽量不统一定义，防止资源浪费
 * 线程池定义在一个统一的工具类方便管理
 * 执行线程通过execute 而不用 submit，，用于线程池异常的统一管理
 *
 * execute执行的线程池异常管理
 * 可通过继承UncaughtExceptionHandler实现uncaughtException方法
 * 然后通过
 * Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler());
 * 全局设置异常执行的类
 * 或者通过自定义ThreadFactory来设置线程异常执行类
 * Thread t = new Thread(r);
 * t.setUncaughtExceptionHandler(new UncaughtExceptionHandler());//设定线程工厂的异常处理器
 *
 *
 * @author zhouzhiqiang
 * @version 1.0
 * @date 2023-01-16 9:54
 */
public class ThreadPoolCustomExecutor {

    private final ThreadPoolExecutor threadPoolExecutor;

    public ThreadPoolCustomExecutor(int corePoolSize, int maximumPoolSize) {
        this(corePoolSize, maximumPoolSize, 60, TimeUnit.SECONDS);
    }

    public ThreadPoolCustomExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit) {
        this(corePoolSize, maximumPoolSize, keepAliveTime, unit, new ArrayBlockingQueue<>(300));
    }

    public ThreadPoolCustomExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    public ThreadPoolCustomExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory) {
        threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
    }

    public ThreadPoolCustomExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler) {
        threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
    }

    public ThreadPoolCustomExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
        threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
    }

    public void execute(Runnable command){
        threadPoolExecutor.execute(command);
    }
}
