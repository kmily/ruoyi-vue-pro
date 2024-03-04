package cn.iocoder.yudao.module.steam.controller.app.vo.buy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 指定商品购买入参
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateByIdReqVo {

    /**
     * 商户订单号
     * 小于等于59位的字符串
     */
    @JsonProperty("merchantOrderNo")
    private String merchantOrderNo;
    /**
     * 收货方的Steam交易链接
     */
    @JsonProperty("tradeLinks")
    private String tradeLinks;
    /**
     * 商品ID
     * 不可为0
     */
    @JsonProperty("commodityId")
    private String commodityId;
    /**
     * 购买最高价
     * 不可为0
     */
    @JsonProperty("purchasePrice")
    private String purchasePrice;

}
