package cn.iocoder.yudao.module.system.controller.admin.helpcenter.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.*;

@Schema(description = "管理后台 - 常见问题更新 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class HelpCenterUpdateReqVO extends HelpCenterBaseVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "14137")
    @NotNull(message = "ID不能为空")
    private Long id;

}
