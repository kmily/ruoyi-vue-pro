package cn.iocoder.yudao.module.oa.controller.admin.projectimpllog.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 工程日志列表创建 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ProjectImplLogCreateReqVO extends ProjectImplLogBaseVO {

    @Schema(description = "更新者")
    private String updater;

}
