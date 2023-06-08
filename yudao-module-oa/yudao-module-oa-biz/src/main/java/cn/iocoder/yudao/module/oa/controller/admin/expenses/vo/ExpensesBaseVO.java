package cn.iocoder.yudao.module.oa.controller.admin.expenses.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

/**
* 报销申请 Base VO，提供给添加、修改、详细的子 VO 使用
* 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
*/
@Data
public class ExpensesBaseVO {

    @Schema(description = "报销类型", required = true, example = "1")
    @NotNull(message = "报销类型不能为空")
    private String expensesType;

    @Schema(description = "展会名称", example = "王五")
    private String exhibitName;

    @Schema(description = "展会开始时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime exhibitBeginDate;

    @Schema(description = "展会结束时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime exhibitEndDate;

    @Schema(description = "展会地点")
    private String exhibitAddress;

    @Schema(description = "关联的拜访过的客户", example = "29466")
    private Long customerId;

    @Schema(description = "费用说明", example = "你猜")
    private String feeRemark;

    @Schema(description = "报销总费用", required = true)
    @NotNull(message = "报销总费用不能为空")
    private BigDecimal fee;

    @Schema(description = "申请单状态", required = true, example = "2")
    @NotNull(message = "申请单状态不能为空")
    private int status;

    @Schema(description = "审批状态", example = "2")
    private int approvalStatus;

    @Schema(description = "备注", example = "你说的对")
    private String remark;

    @Schema(description = "创建者")
    private String creator;

    @Schema(description = "更新者")
    private String updater;

}
