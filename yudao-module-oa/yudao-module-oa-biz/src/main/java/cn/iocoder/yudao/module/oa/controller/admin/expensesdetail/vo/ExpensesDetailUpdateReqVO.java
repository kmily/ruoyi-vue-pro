package cn.iocoder.yudao.module.oa.controller.admin.expensesdetail.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 报销明细更新 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ExpensesDetailUpdateReqVO extends ExpensesDetailBaseVO {

    @Schema(description = "id", required = true, example = "8672")
    @NotNull(message = "id不能为空")
    private Long id;

}
