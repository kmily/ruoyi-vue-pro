package cn.iocoder.yudao.module.steam.service.fin.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiQueryCommodityReqVo implements Serializable {

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
    /**
     * 极速发货购买模式
     * 0：优先购买极速发货；1：只购买极速发货
     */
    @JsonProperty("fastShipping")
    private Integer fastShipping;

}