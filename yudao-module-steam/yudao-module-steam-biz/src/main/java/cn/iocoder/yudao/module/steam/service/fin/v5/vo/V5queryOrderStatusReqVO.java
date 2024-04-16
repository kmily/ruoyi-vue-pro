package cn.iocoder.yudao.module.steam.service.fin.v5.vo;

import lombok.Data;

/**
 * 查询采购订单当前状态
 */
@Data
public class V5queryOrderStatusReqVO {

    // 商户密钥
    private String merchantKey;

    // 商户订单号
    private String merchantOrderNo;

    // v5订单号
    private String orderNo;

}
