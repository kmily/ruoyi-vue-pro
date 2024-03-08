package cn.iocoder.yudao.module.steam.controller.admin.binduser.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.*;

@Schema(description = "管理后台 -  steam用户绑定新增/修改 Request VO")
@Data
public class BindUserSaveReqVO {

    @Schema(description = "steam名称", example = "王五")
    private String steamName;

    @Schema(description = "用户ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "16812")
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    @Schema(description = "SteamId", requiredMode = Schema.RequiredMode.REQUIRED, example = "10768")
    @NotEmpty(message = "SteamId不能为空")
    private String steamId;

    @Schema(description = "交易链接", example = "https://www.iocoder.cn")
    private String tradeUrl;

    @Schema(description = "API KEY")
    private String apiKey;

    @Schema(description = "备注", example = "你说的对")
    private String remark;

    @Schema(description = "steam密码")
    private String steamPassword;

    @Schema(description = "maFile文件")
    private String maFile;

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "32702")
    private Long id;

    @Schema(description = "登录过后的cookie")
    private String loginCookie;

    @Schema(description = "地址池id", example = "28016")
    private Long addressId;

}