package com.cw.module.trade.controller.admin.syncrecord.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 账号同步记录更新 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class SyncRecordUpdateReqVO extends SyncRecordBaseVO {

    @Schema(description = "主键ID", required = true, example = "24232")
    @NotNull(message = "主键ID不能为空")
    private Long id;

}
