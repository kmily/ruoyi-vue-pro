package cn.iocoder.yudao.module.member.controller.app.healthalarmsettings.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;

@Schema(description = "用户 APP - 体征检测雷达设置更新 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AppHealthAlarmSettingsUpdateReqVO extends AppHealthAlarmSettingsBaseVO {

//    @Schema(description = "自增编号", required = true, example = "28225")
//    @NotNull(message = "自增编号不能为空")
    private Long id;

}
