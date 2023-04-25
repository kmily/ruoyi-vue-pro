package cn.iocoder.yudao.module.oa.controller.admin.expenses.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 报销申请更新 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ExpensesUpdateReqVO extends ExpensesBaseVO {

    @Schema(description = "报销id", required = true, example = "6404")
    @NotNull(message = "报销id不能为空")
    private Long id;

}
