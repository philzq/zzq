package zzq.simple.spring.mybatis.util;

public class StringUtils {
    public static boolean isNotEmpty(String str) {
        return !StringUtils.isEmpty(str);
    }

    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }
}
