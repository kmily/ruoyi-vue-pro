package cn.iocoder.yudao.module.steam.controller.app.vo.buy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 指定商品购买 返回值
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateByIdRespVo {
    /**
     * 商户订单号。
     */
    @JsonProperty("merchantOrderNo")
    private String merchantOrderNo;
    /**
     * 订单单号。
     */
    @JsonProperty("orderNo")
    private String orderNo;
    /**
     * 实际支付金额。
     */
    @JsonProperty("payAmount")
    private Double payAmount;
    /**
     * 交易状态：0,成功；2,失败。
     */
    @JsonProperty("orderStatus")
    private Integer orderStatus;
}
