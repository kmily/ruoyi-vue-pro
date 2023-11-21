package cn.iocoder.yudao.module.hospital.controller.admin.careaptitude.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 医护资质审核记录更新 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CareAptitudeCheckLogUpdateReqVO extends CareAptitudeCheckLogBaseVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "25264")
    @NotNull(message = "ID不能为空")
    private Long id;

}
