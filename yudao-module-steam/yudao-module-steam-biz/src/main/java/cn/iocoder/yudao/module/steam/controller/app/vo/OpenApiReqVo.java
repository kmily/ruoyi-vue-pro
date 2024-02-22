package cn.iocoder.yudao.module.steam.controller.app.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Schema(description = "开发平台 -  开发平台接口")
@Data
@ToString(callSuper = true)
public class OpenApiReqVo {
    @Schema(description = "方法", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "方法不能为空")
    private String method;
    @Schema(description = "用户名", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "用户名不能为空")
    private String userName;
    @Schema(description = "数据", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "数据不能为空")
    private String data;
}

