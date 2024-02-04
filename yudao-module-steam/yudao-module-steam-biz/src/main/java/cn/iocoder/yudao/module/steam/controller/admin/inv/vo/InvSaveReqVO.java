package cn.iocoder.yudao.module.steam.controller.admin.inv.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - steam用户库存储新增/修改 Request VO")
@Data
public class InvSaveReqVO {

    @Schema(description = "Primary Key", requiredMode = Schema.RequiredMode.REQUIRED, example = "13481")
    private Integer id;

    @Schema(description = "appid", example = "24254")
    private Integer appid;

    @Schema(description = "assetid", example = "29566")
    private String assetid;

    @Schema(description = "classid", example = "6035")
    private String classid;

    @Schema(description = "instanceid", example = "30735")
    private String instanceid;

    @Schema(description = "amount")
    private String amount;

    @Schema(description = "steamId", requiredMode = Schema.RequiredMode.REQUIRED, example = "5752")
    @NotEmpty(message = "steamId不能为空")
    private String steamId;

    @Schema(description = "启用", example = "1")
    private String status;

}