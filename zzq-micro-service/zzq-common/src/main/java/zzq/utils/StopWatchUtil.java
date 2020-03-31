package zzq.utils;

import org.apache.commons.lang3.time.StopWatch;

import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * 计时器+操作记录辅助类
 * 使用方式
 * StopWatchHelper.calculateOperationTime("Test", stopWatchHelper -> {
 *             stopWatchHelper.add("test1");
 *             stopWatchHelper.add("test2");
 *             stopWatchHelper.add("test3");
 *             stopWatchHelper.add("test4");
 *             stopWatchHelper.functionOperation("Test2", stopWatchHelper1 -> {
 *                 stopWatchHelper.add("test21");
 *                 stopWatchHelper.add("test22");
 *                 stopWatchHelper.add("test23");
 *                 stopWatchHelper.add("test24");
 *                 return null;
 *             });
 *             return null;
 *         });
 */
public class StopWatchUtil {

    /**
     * 计时器
     */
    private StopWatch stopWatch;

    /**
     * 操作记录
     */
    private StringBuilder perfRemark;

    private StopWatchUtil() {
        perfRemark = new StringBuilder();
        perfRemark.append("{");
        stopWatch = StopWatch.createStarted();
    }

    /**
     * 函数操作记录的开始于结束---用于塑造json
     *
     * @param operation
     * @param consumer
     */
    public <R> R functionOperation(String operation, Function<StopWatchUtil, R> consumer) {
        perfRemark.append("\"").append(operation).append("\": {");

        R apply = consumer.apply(this);

        perfRemark.append("},");
        return apply;
    }

    /**
     * 记录操作细节以及操作时间
     *
     * @param operation
     */
    public void add(String operation) {
        if (stopWatch.isStarted()) {
            perfRemark.append("\"").append(operation).append("\":").append(stopWatch.getTime(TimeUnit.MILLISECONDS)).append(",");
        }
    }

    /**
     * 暂停计时器--并将结果记录到操作的上下文中
     */
    private void stop() {
        perfRemark.append("}");
        stopWatch.stop();
        String newPerfRemark = handPerfRemark();
        System.out.println(newPerfRemark);
    }

    /**
     * 处理记录的字符
     * @return
     */
    private String handPerfRemark() {
        return perfRemark.toString().replace(",}", "}");
    }

    /**
     * 计算操作时间
     *
     * @param operation
     * @param func
     */
    public static <R> R calculateOperationTime(String operation, Function<StopWatchUtil, R> func) {
        StopWatchUtil stopWatchUtil = new StopWatchUtil();
        R r = stopWatchUtil.functionOperation(operation, stopWatchUtil1 -> {
            return func.apply(stopWatchUtil);
        });
        stopWatchUtil.stop();
        return r;
    }

    public static void main(String[] args) {
        StopWatchUtil.calculateOperationTime("Test", stopWatchUtil -> {
            stopWatchUtil.add("test1");
            stopWatchUtil.add("test2");
            stopWatchUtil.add("test3");
            stopWatchUtil.add("test4");
            stopWatchUtil.functionOperation("Test2", stopWatchUtil1 -> {
                stopWatchUtil.add("test21");
                stopWatchUtil.add("test22");
                stopWatchUtil.add("test23");
                stopWatchUtil.add("test24");
                return null;
            });
            stopWatchUtil.add("test5");
            return null;
        });
    }
}
