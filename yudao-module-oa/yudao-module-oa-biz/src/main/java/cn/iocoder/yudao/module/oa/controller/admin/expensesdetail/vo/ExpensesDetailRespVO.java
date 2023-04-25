package cn.iocoder.yudao.module.oa.controller.admin.expensesdetail.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 报销明细 Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ExpensesDetailRespVO extends ExpensesDetailBaseVO {

    @Schema(description = "id", required = true, example = "8672")
    private Long id;

}
