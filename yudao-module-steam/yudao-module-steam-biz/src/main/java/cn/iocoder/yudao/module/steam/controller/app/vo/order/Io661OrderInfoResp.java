package cn.iocoder.yudao.module.steam.controller.app.vo.order;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@ToString
public class Io661OrderInfoResp {
    private String orderNo;
    private String merchantNo;
    /**
     * 购买的steamId
     */
    private String steamId;
    /**
     * 是否已支付：[0:未支付 1:已经支付过]
     */
    private Boolean payStatus;
    /**
     * 价格，单位：分
     */
    private Integer paymentAmount;
    private String tradeOfferId;
}
