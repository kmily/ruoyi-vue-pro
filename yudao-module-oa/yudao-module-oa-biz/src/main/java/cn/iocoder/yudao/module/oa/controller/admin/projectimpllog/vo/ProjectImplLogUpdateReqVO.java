package cn.iocoder.yudao.module.oa.controller.admin.projectimpllog.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 工程日志列表更新 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ProjectImplLogUpdateReqVO extends ProjectImplLogBaseVO {

    @Schema(description = "id", required = true, example = "20006")
    @NotNull(message = "id不能为空")
    private Long id;

    @Schema(description = "更新者")
    private String updater;

}
