package cn.iocoder.yudao.module.pay.controller.app.order.vo;

import cn.iocoder.yudao.module.pay.controller.admin.order.vo.PayOrderSubmitRespVO;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.Accessors;

@Schema(description = "用户 APP - 支付订单提交 Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
public class AppPayOrderSubmitRespVO extends PayOrderSubmitRespVO {
    /**
     * UU订单号
     */
    @JsonProperty("orderNo")
    private String orderNo;

    /**
     * 商户订单号
     * orderNo 和 merchantNo 二选一
     * 同时传入以 orderNo 为准
     */
    @JsonProperty("merchantNo")
    private String merchantNo;

}
