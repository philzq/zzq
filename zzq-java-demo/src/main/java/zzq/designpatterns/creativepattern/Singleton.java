package zzq.designpatterns.creativepattern;

import lombok.Data;

/**
 * 单例（Singleton）模式的定义：指一个类只有一个实例，且该类能自行创建这个实例的一种模式。
 * 例如，Windows 中只能打开一个任务管理器，这样可以避免因打开多个任务管理器窗口而造成内存资源的浪费，
 * 或出现各个窗口显示内容的不一致等错误。
 *
 * 在计算机系统中，还有 Windows 的回收站、操作系统中的文件系统、多线程中的线程池、显卡的驱动程序对象、打印机的后台处理服务、应用程序的日志对象、数据库的连接池、网站的计数器、Web 应用的配置对象、应用程序中的对话框、系统中的缓存等常常被设计成单例。
 *
 * 单例模式有 3 个特点：
 * 1.单例类只有一个实例对象；
 * 2.该单例对象必须由单例类自行创建；
 * 3.单例类对外提供一个访问该单例的全局访问点；
 */
public class Singleton {

    /**
     * 懒汉式单例
     *
     * 该模式的特点是类加载时没有生成单例，只有当第一次调用 getlnstance 方法时才去创建这个单例。
     */
    @Data
    public static class LazySingleton{

        /**
         * 使用volatile共享對象内存用於塑造一個線程安全高性能的單例對象
         */
        private volatile static LazySingleton lazySingleton;

        /**
         * 用於測試單例對象是不是同一對象
         */
        private String name;

        private LazySingleton(){};

        public static LazySingleton getInstance(){
            if(lazySingleton == null){
                synchronized (LazySingleton.class){
                    if(lazySingleton == null){
                        lazySingleton = new LazySingleton();
                    }
                }
            }
            return lazySingleton;
        }
    }

    /**
     * 饿汉式单例该模式的特点是类一旦加载就创建一个单例，保证在调用 getInstance 方法之前单例已经存在了。
     */
    @Data
    public static class HungrySingleton{
        private static final HungrySingleton hungrySingleton=new HungrySingleton();

        /**
         * 用於測試單例對象是不是同一對象
         */
        private String name;
        private HungrySingleton(){}
        public static HungrySingleton getInstance()
        {
            return hungrySingleton;
        }
    }

    public static void main(String[] args){
        /*******懒汉式单例模式模式测试********/
        LazySingleton lazySingleton = LazySingleton.getInstance();
        LazySingleton lazySingleton2 = LazySingleton.getInstance();
        lazySingleton.setName("zzq");
        System.out.println(lazySingleton2.getName());//输出zzq，说明lazySingleton与lazySingleton2是同一个对象

        /*******饿汉式单例模式测试********/
        HungrySingleton hungrySingleton = HungrySingleton.getInstance();
        HungrySingleton hungrySingleton2 = HungrySingleton.getInstance();
        hungrySingleton.setName("zzq");
        System.out.println(hungrySingleton2.getName());//输出zzq，说明lazySingleton与lazySingleton2是同一个对象
    }
}
