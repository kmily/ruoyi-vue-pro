package cn.iocoder.yudao.module.oa.controller.admin.expenses.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 报销申请 Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ExpensesRespVO extends ExpensesBaseVO {

    @Schema(description = "报销id", requiredMode = Schema.RequiredMode.REQUIRED, example = "6404")
    private Long id;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

}
