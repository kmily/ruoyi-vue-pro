package cn.iocoder.yudao.module.hospital.controller.admin.medicalcare.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 医护管理更新 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class MedicalCareUpdateReqVO extends MedicalCareBaseVO {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "6966")
    @NotNull(message = "编号不能为空")
    private Long id;

}
