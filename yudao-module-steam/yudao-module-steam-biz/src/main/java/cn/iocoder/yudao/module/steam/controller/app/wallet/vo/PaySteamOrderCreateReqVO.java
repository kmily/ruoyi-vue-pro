package cn.iocoder.yudao.module.steam.controller.app.wallet.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Schema(description = "管理后台 - 示例订单创建 Request VO")
@Data
public class PaySteamOrderCreateReqVO {

    @Schema(description = "出售ID为空", requiredMode = Schema.RequiredMode.REQUIRED, example = "17682")
    @NotNull(message = "出售不能为空")
    private Long sellId;

    @Schema(description = "steamId不能为空", requiredMode = Schema.RequiredMode.REQUIRED, example = "17682")
    @NotNull(message = "steamId不能为空")
    private String steamId;

}
