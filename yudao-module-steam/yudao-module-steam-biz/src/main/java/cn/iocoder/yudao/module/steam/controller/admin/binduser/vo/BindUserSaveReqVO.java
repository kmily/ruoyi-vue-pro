package cn.iocoder.yudao.module.steam.controller.admin.binduser.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

@Schema(description = "管理后台 -  steam用户绑定新增/修改 Request VO")
@Data
public class BindUserSaveReqVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "25045")
    private Integer id;

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

    @Schema(description = "登录名称", example = "赵六")
    private String loginName;

    @Schema(description = "登录密码")
    private String loginPassword;

    @Schema(description = "登录环")
    private String loginSharedSecret;

    @Schema(description = "登录会话")
    private String loginSession;

    @Schema(description = "登录时间")
    private LocalDateTime loginTime;

}