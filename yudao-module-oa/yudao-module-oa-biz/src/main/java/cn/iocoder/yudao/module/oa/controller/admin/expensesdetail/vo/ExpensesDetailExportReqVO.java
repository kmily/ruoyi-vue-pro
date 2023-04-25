package cn.iocoder.yudao.module.oa.controller.admin.expensesdetail.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

@Schema(description = "管理后台 - 报销明细 Excel 导出 Request VO，参数和 ExpensesDetailPageReqVO 是一致的")
@Data
public class ExpensesDetailExportReqVO {

    @Schema(description = "明细类型", example = "1")
    private String detailType;

}
