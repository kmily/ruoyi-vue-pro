package cn.iocoder.yudao.module.hospital.controller.app.careaptitude.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;

@Schema(description = "用户 APP - 医护资质更新 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AppCareAptitudeUpdateReqVO extends AppCareAptitudeBaseVO {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "15828")
    @NotNull(message = "编号不能为空")
    private Long id;

}
