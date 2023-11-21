package cn.iocoder.yudao.module.hospital.controller.admin.medicalcare.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 医护管理创建 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class MedicalCareCreateReqVO extends MedicalCareBaseVO {

    @Schema(description = "密码", requiredMode = Schema.RequiredMode.REQUIRED)
    private String password;

}
