package zzq.thread;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.alibaba.ttl.threadpool.TtlExecutors;
import org.springframework.scheduling.concurrent.DefaultManagedAwareThreadFactory;

import java.util.concurrent.*;

public class ThreadLocalTest {
    public static final TransmittableThreadLocal<String> globalThreadLocal = new TransmittableThreadLocal<>() {
        @Override
        protected String initialValue() {
            return "";
        }
    };

    public static void main(String[] args) throws Exception {
        //这么初始化 remove() 后值就是initialValue设置的值，可以避免空指针问题
        TransmittableThreadLocal<String> transmittableThreadLocal = new TransmittableThreadLocal<>() {
            @Override
            protected String initialValue() {
                return "";
            }
        };
        //两个全局连接池嵌套场景
        ExecutorService executorService1 = new ThreadLocalAwareThreadPool(2, 2, 0, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(), new DefaultManagedAwareThreadFactory(), new ThreadPoolExecutor.AbortPolicy());
        //包装线程池
        executorService1 = TtlExecutors.getTtlExecutorService(executorService1);
        ExecutorService executorService2 = new ThreadLocalAwareThreadPool(1, 2, 0, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(), new DefaultManagedAwareThreadFactory(), new ThreadPoolExecutor.AbortPolicy());
        executorService2 = TtlExecutors.getTtlExecutorService(executorService2);
        for (int i = 0; i < 4; i++) {
            ExecutorService finalExecutorService = executorService2;
            executorService1.execute(() -> {
                try {
                    transmittableThreadLocal.set("parent" + Thread.currentThread().getId() + ":" + transmittableThreadLocal.get());
                    String s = transmittableThreadLocal.get();
                    finalExecutorService.execute(() -> {
                        try {
                            System.out.println(s);
                            transmittableThreadLocal.set("child" + Thread.currentThread().getId() + ":" + transmittableThreadLocal.get());
                            String ss = transmittableThreadLocal.get();
                            System.out.println(ss);
                        } finally {
                            transmittableThreadLocal.remove();
                        }
                    });
                } finally {
                    transmittableThreadLocal.remove();
                }

            });
        }

    }

    public static class ThreadLocalAwareThreadPool extends ThreadPoolExecutor {

        public ThreadLocalAwareThreadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
            super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
        }

        @Override
        protected void afterExecute(Runnable r, Throwable t) {
            //如果用submit需要自行get()获取异常，或者按afterExecute()备注来处理
            super.afterExecute(r, t);
            globalThreadLocal.remove();
        }
    }
}


