package cn.iocoder.yudao.module.steam.controller.app.vo.buy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 指定模板购买入参
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateByTemplateReqVo {

    /**
     * 商户订单号
     */
    @JsonProperty("merchantOrderNo")
    private String merchantOrderNo;
    /**
     * 收货方的Steam交易链接
     */
    @JsonProperty("tradeLinks")
    private String tradeLinks;
    /**
     * 商品模版ID
     */
    @JsonProperty("commodityTemplateId")
    private String commodityTemplateId;
    /**
     * 模板hashname
     */
    @JsonProperty("commodityHashName")
    private String commodityHashName;
    /**
     * 极速发货购买模式
     * 0：优先购买极速发货；1：只购买极速发货
     */
    @JsonProperty("fastShipping")
    private Integer fastShipping;
    /**
     * 购买最高价
     * 不可为0
     */
    @JsonProperty("purchasePrice")
    private String purchasePrice;

}
