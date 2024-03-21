package cn.iocoder.yudao.module.steam.controller.app.binduser.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Schema(description = "管理后台 -  用户解绑")
@Data
@ToString(callSuper = true)
public class AppBindUserApiKeyReqVO {
    @Schema(description = "bindId", example = "123456")
    @NotNull(message = "bindId不能为空")
    private Long bindId;
    @Schema(description = "apiKey", example = "123456")
    @NotNull(message = "apiKey不能为空")
    private String apiKey;
    @Schema(description = "apiKey", example = "123456")
    @NotNull(message = "apiKey不能为空")
    private String tradeUrl;
}