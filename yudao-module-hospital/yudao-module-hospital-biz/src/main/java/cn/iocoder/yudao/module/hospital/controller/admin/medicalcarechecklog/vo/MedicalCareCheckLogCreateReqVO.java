package cn.iocoder.yudao.module.hospital.controller.admin.medicalcarechecklog.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 医护审核记录创建 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class MedicalCareCheckLogCreateReqVO extends MedicalCareCheckLogBaseVO {

}
