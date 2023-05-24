package cn.iocoder.yudao.module.oa.controller.admin.projectimpllog.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Schema(description = "管理后台 - 工程日志列表分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ProjectImplLogPageReqVO extends PageParam {

    @Schema(description = "合同id", example = "1993")
    private Long contractId;

    @Schema(description = "工程进度", example = "1")
    private String implStatus;

    @Schema(description = "创建者")
    private String creator;

}
