package cn.iocoder.yudao.module.oa.controller.admin.projectimpl.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Schema(description = "管理后台 - 工程实施列 Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ProjectImplRespVO extends ProjectImplBaseVO {

    @Schema(description = "id", required = true, example = "25477")
    private Long id;

}
