package cn.iocoder.yudao.module.oa.controller.admin.projectimpllog.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 工程日志列表 Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ProjectImplLogRespVO extends ProjectImplLogBaseVO {

    @Schema(description = "id", required = true, example = "20006")
    private Long id;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

}
