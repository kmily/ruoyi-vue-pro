package cn.iocoder.yudao.module.steam.controller.admin.offlinerechange.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 线下人工充值新增/修改 Request VO")
@Data
public class OfflineRechangeSaveReqVO {

    @Schema(description = "用户ID", example = "17047")
    private Long userId;

    @Schema(description = "用户类型", example = "1")
    private Integer userType;

    @Schema(description = "充值金额")
    private Integer amount;

    @Schema(description = "备注")
    private String comment;

    @Schema(description = "Primary Key", requiredMode = Schema.RequiredMode.REQUIRED, example = "3219")
    private Long id;

    private String mobile;

    private Integer rechangeType;
}