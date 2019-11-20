package zzq.simple.main.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import java.time.LocalDateTime;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class OrderLog {
    /**
     * 日志ID : 
     */
    @ApiModelProperty(value = "日志ID:")
    private Long logID;
}