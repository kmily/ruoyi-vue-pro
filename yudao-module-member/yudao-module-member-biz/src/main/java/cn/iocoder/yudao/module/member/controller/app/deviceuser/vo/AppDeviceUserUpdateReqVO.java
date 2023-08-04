package cn.iocoder.yudao.module.member.controller.app.deviceuser.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;

@Schema(description = "用户 APP - 设备和用户绑定更新 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AppDeviceUserUpdateReqVO extends AppDeviceUserBaseVO {

    @Schema(description = "自增编号", required = true, example = "7201")
    @NotNull(message = "自增编号不能为空")
    private Long id;

}
