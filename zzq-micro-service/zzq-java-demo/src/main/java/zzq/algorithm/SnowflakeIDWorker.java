package zzq.algorithm;


import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.Random;

/**
 * Twitter_Snowflake<br>
 * SnowFlake的结构如下(每部分用-分开):<br>
 * 0 - 0000000000 0000000000 0000000000 0000000000 0 - 00000 - 00000 - 000000000000 <br>
 * 1位标识，由于long基本类型在Java中是带符号的，最高位是符号位，正数是0，负数是1，所以id一般是正数，最高位是0<br>
 * 41位时间截(毫秒级)，注意，41位时间截不是存储当前时间的时间截，而是存储时间截的差值（当前时间截 - 开始时间截)
 * 得到的值），这里的的开始时间截，一般是我们的id生成器开始使用的时间，由我们程序来指定的（如下下面程序IdWorker类的startTime属性）。41位的时间截，可以使用69年，年T = (1L << 41) / (1000L * 60 * 60 * 24 * 365) = 69<br>
 * 10位的数据机器位，可以部署在1024个节点，包括5位datacenterId和5位workerId<br>
 * 12位序列，毫秒内的计数，12位的计数顺序号支持每个节点每毫秒(同一机器，同一时间截)产生4096个ID序号<br>
 * 加起来刚好64位，为一个Long型。<br>
 * SnowFlake的优点是，整体上按照时间自增排序，并且整个分布式系统内不会产生ID碰撞(由数据中心ID和机器ID作区分)，并且效率较高，经测试，SnowFlake每秒能够产生26万ID左右。
 */
public class SnowflakeIDWorker {

    /**
     * 时间部分所占长度
     */
    private static final int TIME_LEN = 41;
    /**
     * 数据中心id所占长度
     */
    private static final int DATA_LEN = 5;
    /**
     * 机器id所占长度
     */
    private static final int WORK_LEN = 5;
    /**
     * 毫秒内存序列所占长度
     */
    private static final int SEQ_LEN = 12;

    /**
     * 定义起始时间 2020-07-27
     */
    private static final long START_TIME = 1595835560497L;
    /**
     * 上次生成ID的时间戳
     */
    private static long LAST_TIME_STAMP = -1L;
    /**
     * 时间部分向左移动的位数 22
     */
    private static final int TIME_LEFT_BIT = 64 - 1 - TIME_LEN;

    /**
     * 自动获取数据中心id（可以手动定义0-31之间的数）
     */
    private static final long DATA_ID = getDataID();
    /**
     * 自动机器id（可以手动定义0-31之间的数）
     */
    private static final long WORK_ID = getWorkID();
    /**
     * 数据中心id最大值 31
     */
    private static final int DATA_MAX_NUM = ~(-1 << DATA_LEN);
    /**
     * 机器id最大值 31
     */
    private static final int WORK_MAX_NUM = ~(-1 << WORK_LEN);
    /**
     * 随机获取数据中心id的参数 32
     */
    private static final int DATA_RANDOM = DATA_MAX_NUM + 1;
    /**
     * 随机获取机器id的参数 32
     */
    private static final int WORK_RANDOM = WORK_MAX_NUM + 1;
    /**
     * 数据中心id左移位数 17
     */
    private static final int DATA_LEFT_BIT = TIME_LEFT_BIT - DATA_LEN;
    /**
     * 机器id左移位数 12
     */
    private static final int WORK_LEFT_BIT = DATA_LEFT_BIT - WORK_LEN;

    /**
     * 上一次毫秒内存序列值
     */
    private static long LAST_SEQ = 0L;
    /**
     * 毫秒内存列的最大值 4095
     */
    private static final long SEQ_MAX_NUM = ~(-1 << SEQ_LEN);

    /**
     * 获取字符串S的字节数组，然后将数组的元素相加，对（max+1）取余
     *
     * @param s   本地机器的hostName/hostAddress
     * @param max 机房/机器的id最大值
     * @return
     */
    private static int getHostID(String s, int max) {
        byte[] bytes = s.getBytes();
        int sums = 0;
        for (int b : bytes) {
            sums += b;
        }
        return sums % (max + 1);
    }

    /**
     * 根据 host address 取余， 发送异常就返回 0-31 之间的随机数
     *
     * @return 机器ID
     */
    private static int getWorkID() {
        try {
            return getHostID(Inet4Address.getLocalHost().getHostAddress(), WORK_MAX_NUM);
        } catch (UnknownHostException e) {
            return new Random().nextInt(WORK_RANDOM);
        }
    }

    /**
     * 根据 host name 取余， 发送异常就返回 0-31 之间的随机数
     *
     * @return 机房ID（数据中心ID）
     */
    private static int getDataID() {
        try {
            return getHostID(Inet4Address.getLocalHost().getHostName(), DATA_MAX_NUM);
        } catch (Exception e) {
            return new Random().nextInt(DATA_RANDOM);
        }
    }

    /**
     * 获取下一不同毫秒的时间戳
     *
     * @param lastMillis
     * @return 下一毫秒的时间戳
     */
    private static long nextMillis(long lastMillis) {
        long now = System.currentTimeMillis();
        while (now <= lastMillis) {
            now = System.currentTimeMillis();
        }
        return now;
    }

    /**
     * 核心算法，需要加锁保证并发安全
     *
     * @return 返回唯一ID
     */
    public synchronized static long getSnowflakeID() {
        long now = System.currentTimeMillis();

        // 如果当前时间小于上一次ID生成的时间戳，说明系统时钟回退过，此时因抛出异常
        if (now < LAST_TIME_STAMP) {
            throw new RuntimeException(String.format("系统时间错误！ %d 毫秒内拒绝生成雪花ID", START_TIME));
        }

        if (now == LAST_TIME_STAMP) {
            LAST_SEQ = (LAST_SEQ + 1) & SEQ_MAX_NUM;
            if (LAST_SEQ == 0) {
                now = nextMillis(LAST_TIME_STAMP);
            }
        } else {
            LAST_SEQ = 0;
        }

        // 上次生成ID的时间戳
        LAST_TIME_STAMP = now;

        return ((now - START_TIME) << TIME_LEFT_BIT | (DATA_ID << DATA_LEFT_BIT) | (WORK_ID << WORK_LEFT_BIT) | LAST_SEQ);
    }

    /**
     * 主函数测试
     *
     * @param args
     */
    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        int num = 30000;
        for (int i = 0; i < num; i++) {
            System.out.println(getSnowflakeID());
        }
        long end = System.currentTimeMillis();

        System.out.println("共生成 " + num + " 个ID，用时 " + (end - start) + " 毫秒");
    }
}
