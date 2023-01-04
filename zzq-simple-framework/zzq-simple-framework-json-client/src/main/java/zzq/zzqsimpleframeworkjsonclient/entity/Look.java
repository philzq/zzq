package zzq.zzqsimpleframeworkjsonclient.entity;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * @author zhouzhiqiang
 * @version 1.0
 * @date 2023-01-04 14:13
 */
@Data
public class Look {
    private String str;

    private Boolean aBoolean;

    private LocalDate localDate;

    private LocalDateTime localDateTime;

    private LocalTime localTime;

    private Double aDouble;

}
