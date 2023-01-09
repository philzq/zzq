package zzq.zzqsimpleframeworkcommon.entity;

import java.time.format.DateTimeFormatter;

/**
 * 日期格式
 *
 * @author zhouzhiqiang
 * @version 1.0
 * @date 2023-01-09 17:14
 */
public interface DateFormatConstant {

    /** 时间格式(yyyy-MM-dd) */
    String DATE_PATTERN = "yyyy-MM-dd";
    /** 时间格式(yyyy-MM-dd HH:mm:ss) */
    String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    /** 时间格式(HH:mm:ss) */
    String TIME_PATTERN = "HH:mm:ss";

    /** 时间格式(yyyy-MM-dd HH:mm:ss) */
    DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN);

    /** 时间格式(yyyy-MM-dd) */
    DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_PATTERN);

    /** 时间格式(HH:mm:ss) */
    DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern(TIME_PATTERN);
}
