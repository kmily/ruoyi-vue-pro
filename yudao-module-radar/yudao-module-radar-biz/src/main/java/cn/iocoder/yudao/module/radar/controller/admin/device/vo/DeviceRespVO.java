package cn.iocoder.yudao.module.radar.controller.admin.device.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 设备信息 Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DeviceRespVO extends DeviceBaseVO {

    @Schema(description = "设备ID", required = true, example = "2198")
    private Long id;

    @Schema(description = "创建时间", required = true)
    private LocalDateTime createTime;

}
