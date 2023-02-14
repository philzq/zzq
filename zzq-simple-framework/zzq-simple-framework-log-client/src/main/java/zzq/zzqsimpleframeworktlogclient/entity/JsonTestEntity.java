package zzq.zzqsimpleframeworktlogclient.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * json序列化反序列化测试实体
 *
 * @author zhouzhiqiang
 * @version 1.0
 * @date 2023-01-04 14:11
 */
@Data
public class JsonTestEntity {

    private String str;

    private Boolean aBoolean;

    private LocalDate localDate;

    private LocalDateTime localDateTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private LocalDateTime localDateTime2;

    private LocalTime localTime;

    private Double aDouble;

    private Look look;
}
