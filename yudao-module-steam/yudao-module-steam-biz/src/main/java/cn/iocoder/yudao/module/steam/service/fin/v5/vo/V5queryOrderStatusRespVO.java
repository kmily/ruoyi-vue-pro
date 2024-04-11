package cn.iocoder.yudao.module.steam.service.fin.v5.vo;

import lombok.Data;


/**
 * 查询采购订单当前状态
 */
@Data
public class V5queryOrderStatusRespVO {

    // 商户订单号
    private String merchantOrderNo;

    // v5订单号
    private String orderNo;

    // 订单金额
    private Integer orderAmount;

    // 订单状态
    private Integer status;

    // 订单状态描述
    private String statusMsg;

    // 发货状态
    private Integer deliverStatus;

    // 发货状态描述
    private String deliverMsg;
}
