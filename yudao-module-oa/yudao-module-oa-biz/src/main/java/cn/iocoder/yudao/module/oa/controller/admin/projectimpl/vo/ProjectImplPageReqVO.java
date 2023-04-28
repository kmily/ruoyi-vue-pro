package cn.iocoder.yudao.module.oa.controller.admin.projectimpl.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

@Schema(description = "管理后台 - 工程实施列分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ProjectImplPageReqVO extends PageParam {

    @Schema(description = "合同id", example = "10972")
    private Long contractId;

    @Schema(description = "实施范围")
    private String implScope;

}
