package cn.iocoder.yudao.module.steam.service.fin.v5.vo;

import lombok.Data;

@Data
public class V5OrderInfo {
    private String merchantKey;
    private String merchantOrderNo;
    private String orderNo;

    public V5OrderInfo(String merchantKey, String merchantOrderNo, String orderNo) {
        this.merchantKey = merchantKey;
        this.merchantOrderNo = merchantOrderNo;
        this.orderNo = orderNo;
    }


}