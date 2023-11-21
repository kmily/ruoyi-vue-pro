package cn.iocoder.yudao.module.hospital.controller.app.careaptitude.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.*;

@Schema(description = "用户 APP - 医护资质创建 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AppCareAptitudeCreateReqVO extends AppCareAptitudeBaseVO {

}
