package cn.iocoder.yudao.module.steam.service.fin.v5.vo;

import lombok.Data;

@Data
public class V5cancelOrderRespVO {
    // 订单状态
    private String status;
    // 订单状态描述
    private String statusMsg;
    // 商户订单号
    private String merchantOrderNo;
}
