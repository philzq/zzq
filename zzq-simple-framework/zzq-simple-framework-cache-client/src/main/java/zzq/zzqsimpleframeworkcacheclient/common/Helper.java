package zzq.zzqsimpleframeworkcacheclient.common;

public class Helper {
    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ignored) {
        }
    }
}
