package cn.iocoder.yudao.module.hospital.controller.admin.aptitude.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 资质信息更新 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AptitudeUpdateReqVO extends AptitudeBaseVO {

    @Schema(description = "收藏编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "21395")
    @NotNull(message = "收藏编号不能为空")
    private Long id;

}
