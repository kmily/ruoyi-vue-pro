package com.cw.module.trade.controller.admin.account.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 交易账号 Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AccountRespVO extends AccountBaseVO {

    @Schema(description = "主键ID", required = true, example = "23234")
    private Long id;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

}
