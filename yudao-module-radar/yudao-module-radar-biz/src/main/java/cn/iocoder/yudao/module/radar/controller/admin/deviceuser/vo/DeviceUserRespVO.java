package cn.iocoder.yudao.module.radar.controller.admin.deviceuser.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 设备和用户绑定 Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DeviceUserRespVO extends DeviceUserBaseVO {

    @Schema(description = "自增编号", required = true, example = "250")
    private Long id;

    @Schema(description = "创建时间", required = true)
    private LocalDateTime createTime;

}
