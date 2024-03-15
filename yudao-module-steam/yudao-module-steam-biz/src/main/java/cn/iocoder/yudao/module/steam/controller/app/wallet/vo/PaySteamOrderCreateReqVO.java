package cn.iocoder.yudao.module.steam.controller.app.wallet.vo;

import cn.iocoder.yudao.module.steam.enums.PlatFormEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Schema(description = "管理后台 - 示例订单创建 Request VO")
@Data
public class PaySteamOrderCreateReqVO implements Serializable {

    @Schema(description = "出售ID为空", requiredMode = Schema.RequiredMode.REQUIRED, example = "17682")
    @NotNull(message = "出售不能为空")
    private Long sellId;

    @Schema(description = "steamId不能为空", requiredMode = Schema.RequiredMode.REQUIRED, example = "17682")
    @NotNull(message = "steamId不能为空")
    private String steamId;
    @Schema(description = "platform不能为空", requiredMode = Schema.RequiredMode.REQUIRED, example = "17682")
//    @NotNull(message = "platform不能为空")
    private PlatFormEnum platform;
    /**
     * 商户订单号
     */
    @Schema(description = "steamId不能为空", requiredMode = Schema.RequiredMode.REQUIRED, example = "17682")
    @NotNull(message = "商户订单号不能为空")
    private String merchantNo;
}
