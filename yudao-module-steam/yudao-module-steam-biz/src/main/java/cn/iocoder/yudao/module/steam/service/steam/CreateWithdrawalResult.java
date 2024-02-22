package cn.iocoder.yudao.module.steam.service.steam;

import lombok.Data;

import java.io.Serializable;

@Data
public class CreateWithdrawalResult implements Serializable {
    /**
     * 提现ID
     */
    private Long withdrawalId;
    /**
     * 支付订单ID
     */
    private Long payOrderId;
}
