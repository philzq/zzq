package zzq.zzqsimpleframeworklog.entity;

import zzq.zzqsimpleframeworklog.entity.common.BaseGlobalLogEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author zhouzhiqiang
 * @version 1.0
 * @date 2023-02-09 18:13
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class OrderMonitorLogEntity extends BaseGlobalLogEntity {

    private String requestType;
    private String partnerId;
    private String waybillCode;

    private String mobile ;
    private String name ;
    private String  orderCode ;
    private String complaintNumber ;
    private String  logisticCode ;
    private String kdnOrderCode ;
    private Integer  complaintType ;
    private String complaintContent ;

    private String resultCode;
    private String resultDesc;
    private String monthCode;
    private String payType;
}
