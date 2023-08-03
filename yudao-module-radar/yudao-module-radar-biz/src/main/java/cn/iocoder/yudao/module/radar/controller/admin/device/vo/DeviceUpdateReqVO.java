package cn.iocoder.yudao.module.radar.controller.admin.device.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 设备信息更新 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DeviceUpdateReqVO extends DeviceBaseVO {

    @Schema(description = "设备ID", required = true, example = "2198")
    @NotNull(message = "设备ID不能为空")
    private Long id;

}
