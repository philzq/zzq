package zzq.simple.mybatis.common;

public class Restarter {

    /**
     * 获取启动类全限定名
     *
     * @param thread
     * @return
     */
    public static String getMainClassName(Thread thread) {
        for (StackTraceElement element : thread.getStackTrace()) {
            if ("main".equals(element.getMethodName())) {
                return element.getClassName();
            }
        }
        throw new IllegalStateException("Unable to find main method");
    }
}
