package zzq;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.*;

public class Test {

    public static void main(String[] args) throws Exception{
        Date date = new Date(1577414214653L);
        System.out.println(date);
        /*int corePoolSize = 5;
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
        System.in.read();*/
    }

    private static void test(String... params){
        test2(params);
    }

    private static void test2(String... params){
        for(String param : params){
            System.out.println(param);
        }
    }
}
