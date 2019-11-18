package zzq.simple.mybatis.entity;

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

    /**
     * 订单号 : 
     */
    @ApiModelProperty(value = "订单号:")
    private String orderNo;

    /**
     * 操作员 : 
     */
    @ApiModelProperty(value = "操作员:")
    private String operator;

    /**
     * 操作时间 : 
     */
    @ApiModelProperty(value = "操作时间:")
    private LocalDateTime operateTime;

    /**
     * GUID : 
     */
    @ApiModelProperty(value = "GUID:")
    private String sendGUID;

    private Integer sendType;

    /**
     * 操作内容 : 
     */
    @ApiModelProperty(value = "操作内容:")
    private String operateContent;

    /**
     * 系统备注 : 
     */
    @ApiModelProperty(value = "系统备注:")
    private String systemRemark;
}