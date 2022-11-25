package cn.iocoder.yudao.module.pay.controller.admin.merchant.vo.app;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@ApiModel("管理后台 - 支付应用信息更新 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class PayAppUpdateReqVO extends PayAppBaseVO {

    @ApiModelProperty(value = "应用编号", required = true)
    @NotNull(message = "应用编号不能为空")
    private Long id;

}
