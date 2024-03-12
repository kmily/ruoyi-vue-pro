package cn.iocoder.yudao.module.steam.controller.app.wallet.vo;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Schema(description = "管理后台 - 提现订单 Request VO")
@Data
public class PayWithdrawalOrderCreateReqVO {

    @Schema(description = "提现金额", requiredMode = Schema.RequiredMode.REQUIRED, example = "17682")
    @NotNull(message = "提现金额")
    private Integer amount;
    @Schema(description = "提现信息", requiredMode = Schema.RequiredMode.REQUIRED, example = "银行号1234565")
    @NotNull(message = "提现信息不能为空")
    @Valid
    @JsonUnwrapped
    private WithdrawalInfo withdrawalInfo;

}
