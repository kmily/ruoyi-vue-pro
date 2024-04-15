package cn.iocoder.yudao.module.steam.service.fin.vo;

import lombok.Data;

/**
 * 回调结果
 */
@Data
public class ApiProcessNotifyResp {
    /**
     * 主订单号
     */
    private Long orderId;
    /**
     * 第三方订单号
     */
    private String orderNo;
    /**
     * 发货状态描述
     */
    private String deliverStatusDesc;

    /**
     * 失败原因
     */
    private String failReason;
}
