package cn.iocoder.yudao.module.oa.controller.admin.opportunity.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

@Schema(description = "管理后台 - 商机分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class OpportunityPageReqVO extends PageParam {

    @Schema(description = "商机标题")
    private String businessTitle;

    @Schema(description = "商机状态", example = "2")
    private String status;

    @Schema(description = "创建者")
    private String creator;

}
