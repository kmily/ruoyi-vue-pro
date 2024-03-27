package cn.iocoder.yudao.module.steam.controller.app.devaccount.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Schema(description = "管理后台 - 开放平台用户新增/修改 Request VO")
@Data
public class AppDevAccountSaveReqVO {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "32763")
    private Long id;

    @Schema(description = "用户ID", example = "19141")
    private Long userId;

    @Schema(description = "api用户名", requiredMode = Schema.RequiredMode.REQUIRED, example = "张三")
    private String userName;

    @Schema(description = "公匙")
    private String apiPublicKey;

    @Schema(description = "回调地址", example = "29689")
    private String callbackUrl;

    @Schema(description = "状态", example = "2")
    private Integer status;

    @Schema(description = "用户类型", example = "1")
    private Integer userType;
    @Schema(description = "真实姓名", example = "1")
//    @NotEmpty(message = "真实姓名不能为空")
    private String trueName;
    @Schema(description = "身份证号", example = "1")
//    @NotEmpty(message = "身份证号不能为空")
    private String idNum;
    @Schema(description = "申请理由", example = "1")
//    @NotEmpty(message = "申请理由不能为空")
    private String applyReason;

}