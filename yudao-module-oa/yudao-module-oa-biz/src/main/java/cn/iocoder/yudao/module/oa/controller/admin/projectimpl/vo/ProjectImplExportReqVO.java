package cn.iocoder.yudao.module.oa.controller.admin.projectimpl.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

@Schema(description = "管理后台 - 工程实施列 Excel 导出 Request VO，参数和 ProjectImplPageReqVO 是一致的")
@Data
public class ProjectImplExportReqVO {

    @Schema(description = "合同id", example = "10972")
    private Long contractId;

}
