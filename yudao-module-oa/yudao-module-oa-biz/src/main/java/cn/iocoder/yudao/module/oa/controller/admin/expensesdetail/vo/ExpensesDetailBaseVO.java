package cn.iocoder.yudao.module.oa.controller.admin.expensesdetail.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import javax.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

/**
* 报销明细 Base VO，提供给添加、修改、详细的子 VO 使用
* 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
*/
@Data
public class ExpensesDetailBaseVO {

    @Schema(description = "明细类型", required = true, example = "1")
    @NotNull(message = "明细类型不能为空")
    private String detailType;

    @Schema(description = "消费时间", required = true)
    @NotNull(message = "消费时间不能为空")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime consumeTime;

    @Schema(description = "报销费用", required = true)
    @NotNull(message = "报销费用不能为空")
    private BigDecimal detailFee;

    @Schema(description = "明细备注", example = "你说的对")
    private String detailRemark;

    @Schema(description = "报销申请id", required = true, example = "4031")
    @NotNull(message = "报销申请id不能为空")
    private Long expensesId;

}
