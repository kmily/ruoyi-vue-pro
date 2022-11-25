package cn.iocoder.yudao.module.pay.controller.admin.merchant.vo.channel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@ApiModel("管理后台 - 支付渠道 更新 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class PayChannelUpdateReqVO extends PayChannelBaseVO {

    @ApiModelProperty(value = "商户编号", required = true)
    @NotNull(message = "商户编号不能为空")
    private Long id;

    @ApiModelProperty(value = "渠道配置的json字符串")
    @NotBlank(message = "渠道配置不能为空")
    private String config;
}
