package cn.iocoder.yudao.module.oa.controller.admin.projectimpllog.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

@Schema(description = "管理后台 - 工程日志列表 Excel 导出 Request VO，参数和 ProjectImplLogPageReqVO 是一致的")
@Data
public class ProjectImplLogExportReqVO {

    @Schema(description = "合同id", example = "1993")
    private Long contractId;

    @Schema(description = "工程进度", example = "1")
    private String implStatus;

    @Schema(description = "创建者")
    private String createBy;

}
