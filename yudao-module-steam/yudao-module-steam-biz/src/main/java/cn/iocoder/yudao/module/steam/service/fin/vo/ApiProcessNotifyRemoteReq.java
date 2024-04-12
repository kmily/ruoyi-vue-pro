package cn.iocoder.yudao.module.steam.service.fin.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 回调结果
 */
@Data
public class ApiProcessNotifyRemoteReq implements Serializable {
    /**
     * 主订单号
     */
    private String merchantNo;
    /**
     * 第三方订单号
     */
    private String orderNo;
    /**
     * 交易阶段1,支付；2,发货，3资金结算
     */
    private Integer orderStatus;
    /**
     * 支付状态
     */
    private Integer payOrderStatus;
    /**
     * 支付状态
     */
    private String payOrderStatusText;
    /**
     * 资金状态
     */
    private Integer cashStatus;
    /**
     * 资金状态
     */
    private String cashStatusText;
    /**
     * 发货状态
     * IvnStatusEnum
     */
    private Integer invStatus;
    /**
     * 发货状态说明
     */
    private String invStatusText;
}
