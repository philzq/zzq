package zzq.thread;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ArrayBlockingQueueTest {

    public static void main(String[] args) throws Exception{
        int corePoolSize = 5;
        int maximumPoolSize = 10;
        int blockingQueueSize = 10;
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                corePoolSize, //最小线程数
                maximumPoolSize, //最大线程数
                100, TimeUnit.MILLISECONDS, //超过最小线程数的线程，空闲多久则回收
                new ArrayBlockingQueue<>(blockingQueueSize, true)//放到某个队列里
        );
        //是否去掉非活动线程
        threadPoolExecutor.allowCoreThreadTimeOut(true);
        //默认创建最小线程数的线程
        threadPoolExecutor.prestartAllCoreThreads();
        Set<Long> threadId = new HashSet<>();
        long threadsStartTime = System.currentTimeMillis();
        for (int i = 0; i < 20; i++) {
            threadPoolExecutor.execute(() -> {
                try {
                    long threadStartTime = System.currentTimeMillis();
                    Thread thread = Thread.currentThread();
                    long id = thread.getId();
                    long subTime = threadStartTime - threadsStartTime;
                    System.out.println("threadsStartTime:"+threadsStartTime+",threadStartTime:"+threadStartTime+",subTime:"+ subTime +",id:"+id);
                    Thread.sleep(3000);
                    if(threadId.contains(id)){
                        System.out.println("已存在的线程执行任务:"+id +":"+thread.getName());
                    }else{
                        threadId.add(id);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
        System.in.read();
    }

}
