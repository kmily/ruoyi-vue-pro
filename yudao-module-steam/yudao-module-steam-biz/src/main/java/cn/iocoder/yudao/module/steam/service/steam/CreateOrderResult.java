package cn.iocoder.yudao.module.steam.service.steam;

import lombok.Data;

import java.io.Serializable;

@Data
public class CreateOrderResult implements Serializable {
    /**
     * 业务单据
     */
    private Long bizOrderId;
    /**
     * 支付订单ID
     */
    private Long payOrderId;
}
