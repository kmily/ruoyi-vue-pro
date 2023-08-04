package cn.iocoder.yudao.module.member.controller.app.deviceuser.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.*;

@Schema(description = "用户 APP - 设备和用户绑定创建 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AppDeviceUserCreateReqVO extends AppDeviceUserBaseVO {

    @Schema(description = "设备编号", required = true, example = "26138")
    private String deviceCode;

}
