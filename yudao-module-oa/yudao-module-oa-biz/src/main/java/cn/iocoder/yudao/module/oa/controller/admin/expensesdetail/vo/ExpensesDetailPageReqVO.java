package cn.iocoder.yudao.module.oa.controller.admin.expensesdetail.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

@Schema(description = "管理后台 - 报销明细分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ExpensesDetailPageReqVO extends PageParam {

    @Schema(description = "明细类型", example = "1")
    private String detailType;

}
