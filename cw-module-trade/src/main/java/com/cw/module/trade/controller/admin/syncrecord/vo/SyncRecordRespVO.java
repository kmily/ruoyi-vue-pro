package com.cw.module.trade.controller.admin.syncrecord.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 账号同步记录 Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class SyncRecordRespVO extends SyncRecordBaseVO {

    @Schema(description = "主键ID", required = true, example = "24232")
    private Long id;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

}
