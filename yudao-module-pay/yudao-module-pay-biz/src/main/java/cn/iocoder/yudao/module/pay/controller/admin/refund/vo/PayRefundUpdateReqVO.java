package cn.iocoder.yudao.module.pay.controller.admin.refund.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@ApiModel("管理后台 - 退款订单更新 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class PayRefundUpdateReqVO extends PayRefundBaseVO {

    @ApiModelProperty(value = "支付退款编号", required = true)
    @NotNull(message = "支付退款编号不能为空")
    private Long id;

}
