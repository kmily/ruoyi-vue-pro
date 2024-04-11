package cn.iocoder.yudao.module.steam.service.fin.v5.vo;

import lombok.Data;

import java.io.Serializable;
@Data
public class V5BuyProductVo implements Serializable {
    private String marketHashName;
    private int purchasePrice;
    private String tradeUrl;
    private String merchantOrderNo;
    private String merchantKey;
    private int buyType;

    public V5BuyProductVo(String marketHashName, int purchasePrice, String tradeUrl, String merchantOrderNo, String merchantKey, int buyType) {
        this.marketHashName = marketHashName;
        this.purchasePrice = purchasePrice;
        this.tradeUrl = tradeUrl;
        this.merchantOrderNo = merchantOrderNo;
        this.merchantKey = merchantKey;
        this.buyType = buyType;
    }
}
