package cn.iocoder.yudao.module.steam.service.fin.v5.vo;

import lombok.Data;

@Data
public class CallBackInfoVO {

    private String orderNo;
    private String merchantNo;
    private Integer userId;
    private Integer orderStatus;
    private String orderStatusDesc;
    private Integer deliverStatus;
    private String deliverStatusDesc;
    private String failReason;
}
