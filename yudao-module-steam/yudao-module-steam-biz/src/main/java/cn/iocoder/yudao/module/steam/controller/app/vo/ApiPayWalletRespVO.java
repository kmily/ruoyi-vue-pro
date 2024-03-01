package cn.iocoder.yudao.module.steam.controller.app.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ApiPayWalletRespVO {
    @Schema(description = "金额", requiredMode = Schema.RequiredMode.REQUIRED, example = "100")
    private BigDecimal amount;

    @Schema(description = "用户ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1000")
    private Integer userId;
}
