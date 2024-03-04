package cn.iocoder.yudao.module.steam.controller.admin.devaccount.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotEmpty;

@Schema(description = "管理后台 - 开放平台用户新增/修改 Request VO")
@Data
public class DevAccountSaveReqVO {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "32763")
    private Long id;

    @Schema(description = "用户ID", example = "19141")
    private Long userId;

    @Schema(description = "api用户名", requiredMode = Schema.RequiredMode.REQUIRED, example = "张三")
    private String userName;

    @Schema(description = "公匙")
    @NotEmpty(message = "公钥不能为空")
    private String apiPublicKey;

    @Schema(description = "steam用户 ID", example = "29689")
    private String steamId;

    @Schema(description = "状态", example = "2")
    private Integer status;

    @Schema(description = "用户类型", example = "1")
    private Integer userType;

}