package com.cw.module.trade.controller.admin.position.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 账户持仓信息 Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class PositionRespVO extends PositionBaseVO {

    @Schema(description = "主键ID", required = true, example = "9514")
    private Long id;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

}
