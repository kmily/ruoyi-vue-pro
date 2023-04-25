package cn.iocoder.yudao.module.oa.controller.admin.expenses.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

@Schema(description = "管理后台 - 报销申请分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ExpensesPageReqVO extends PageParam {

    @Schema(description = "报销类型", example = "1")
    private String expensesType;

    @Schema(description = "展会名称", example = "王五")
    private String exhibitName;

    @Schema(description = "审批状态", example = "2")
    private Boolean approvalStatus;

    @Schema(description = "创建者")
    private String createBy;

}
