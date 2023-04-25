package cn.iocoder.yudao.module.oa.controller.admin.opportunity.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import javax.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

/**
* 商机 Base VO，提供给添加、修改、详细的子 VO 使用
* 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
*/
@Data
public class OpportunityBaseVO {

    @Schema(description = "商机标题", required = true)
    @NotNull(message = "商机标题不能为空")
    private String businessTitle;

    @Schema(description = "商机详情")
    private String detail;

    @Schema(description = "上报时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime reportTime;

    @Schema(description = "跟进用户id", example = "199")
    private Long followUserId;

    @Schema(description = "商机状态", example = "2")
    private String status;

    @Schema(description = "备注", example = "随便")
    private String remark;

    @Schema(description = "创建者")
    private String createBy;

    @Schema(description = "更新者")
    private String updateBy;

}
