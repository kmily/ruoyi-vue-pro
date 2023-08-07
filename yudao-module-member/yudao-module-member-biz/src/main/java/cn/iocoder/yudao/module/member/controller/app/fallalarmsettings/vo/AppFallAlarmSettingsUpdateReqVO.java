package cn.iocoder.yudao.module.member.controller.app.fallalarmsettings.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;

@Schema(description = "用户 APP - 跌倒雷达设置更新 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AppFallAlarmSettingsUpdateReqVO extends AppFallAlarmSettingsBaseVO {

//    @Schema(description = "自增编号", required = true, example = "8559")
//    @NotNull(message = "自增编号不能为空")
    private Long id;

}
